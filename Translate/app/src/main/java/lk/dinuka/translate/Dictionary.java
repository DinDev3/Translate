package lk.dinuka.translate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lk.dinuka.translate.databases.english.EnglishEntered;
import lk.dinuka.translate.databases.english.EnglishRepository;
import lk.dinuka.translate.services.MyDictionaryAdapter;

import static lk.dinuka.translate.MainActivity.allEnglishFromDB;
import static lk.dinuka.translate.MainActivity.languageCodes;

public class Dictionary extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private RecyclerView recyclerView;
    private MyDictionaryAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public String selectedSpinnerLanguage;         // holds the translation language Name chosen from the spinner

    private LanguageTranslator translationService;          // translation service

    private String translationLanguageCode;             // used to pass in the translation code of the chosen language
    private String translationText;                     // The English text that is required to be translated will be held in this variable
    private String translatedText;              // The translated text in the desired language

    private int updatingPosition;               // used to get the position of the phrase that's being translated, to update

    static List<String> allTranslationsOfChosen = new ArrayList<>();           // stores all the translated words of the desired language,
    ArrayList<String> allSubscribedLanguages = new ArrayList<>();               //shows all subscribed languages
    public static ArrayList<String> savedLanguages = new ArrayList<>();        // holds changes in saved languages (languages that have been clicked by the user)



    // to pass it into the recyclerview adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        savedLanguages.add("Spanish");
        savedLanguages.add("Arabic");


//        receiveData();

        // open activity Dictionary Language Subscriptions List
        Button showLangListButton = findViewById(R.id.subscribe_button);
        showLangListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dictionary.this, DictionarySubscriptions.class);
                startActivity(intent);
            }
        });

        // -----------------------

        recyclerView = findViewById(R.id.display_recycler_view);
        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify the adapter (a bridge between a UI component and a data source)
        mAdapter = new MyDictionaryAdapter(allEnglishFromDB, allTranslationsOfChosen);          // insert initial list of words here
        recyclerView.setAdapter(mAdapter);

        // -----------------------
        // create array of subscribed languages to pass into spinner
        getAllSubscriptionsArray();


        //---------Spinner

        // create the spinner
        Spinner spinner = findViewById(R.id.dic_language_spinner);

        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        // Create ArrayAdapter using the string array and default spinner layout.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, savedLanguages);


        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String spinnerLabel = adapterView.getItemAtPosition(i).toString();
        selectedSpinnerLanguage = spinnerLabel;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void displayRecords(View view) {      // display english phrases with translations
        receiveData();
    }


    public void updateRecords(View view) {      // update translations of newly added english phrases into the db
                updatingPosition = 0;       // resetting update position

//        if (allEnglishFromDB.size() > allTranslationsOfChosen.size()) {        // if new words have been added -- can't check from this
                                                                                        // null adds up to the size

            if (allTranslationsOfChosen.contains(null)) {
                // update only if null values exists
                for (int i = 0; i < allEnglishFromDB.size(); i++) {
                    updateTranslation("Spanish", allEnglishFromDB.get(i));            // update translations in db
                }
            }

//        }
//        System.out.println("**"+allTranslationsOfChosen);
    }


    public void getAllSubscriptionsArray() {

        for (Map.Entry<String, Boolean> entry : MainActivity.foreignLanguageSubs.entrySet()) {         //checking for all HashMap entries
            if (entry.getValue() == true) {     // add languages that have been subscribed to by the user
                allSubscribedLanguages.add(entry.getKey());              //adding language name into allSubscribedLanguages arrayList
            }
        }
        Collections.sort(allSubscribedLanguages);           // sorting all languages received in alphabetical order (because HashMap has no order)
    }


    // ----------------------
    public void receiveData() {        // used to display english phrase with one translation
        // get all english phrases from db and display
        EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

        englishRepository.getEnglishFromDB().observe(this, new Observer<List<EnglishEntered>>() {
            @Override
            public void onChanged(@Nullable List<EnglishEntered> allEnglish) {
                allTranslationsOfChosen.clear();            // clearing existing data
                for (EnglishEntered english : allEnglish) {
//                     can use these to check data of received records in console
                    System.out.println(english.getEnglish());

//                    use switch case to receive the desired language here>>>>>>>>>>>>??????????????????????
//                    add received translations to allTranslationsOfChosen
                    if (english.getTranslationLang0() != null) {
                        allTranslationsOfChosen.add(english.getTranslationLang0());
                    } else {
                        allTranslationsOfChosen.add(null);
                    }

                    System.out.println(english.getTranslationLang0());

                    mAdapter.notifyDataSetChanged();        // display changes in recyclerview
                }
            }
        });
    }



//  ---------------- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    private LanguageTranslator initLanguageTranslatorService() {           // connect & initiate to the cloud translation service
        Authenticator authenticator = new IamAuthenticator("2daMreRDE8V5zPRO3enCVHGUCH1sQJs-Kdq8ryPn4-ij");

        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);

        service.setServiceUrl("https://api.us-south.language-translator.watson.cloud.ibm.com/instances/caf1b5bc-ff11-4271-96cf-93372088290d");

        return service;
    }


    private class TranslationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            TranslateOptions translateOptions = new TranslateOptions.Builder()
                    .addText(params[0])
                    .source(Language.ENGLISH)
                    .target(params[1])    // pass in translationLanguage here, to get required language
                    .build();

            TranslationResult result = translationService.translate(translateOptions).execute().getResult();

            String firstTranslation = result.getTranslations().get(0).getTranslation();

            return firstTranslation;
        }

        @Override
        protected void onPostExecute(String translatedText) {       // update one phrase at a time
            super.onPostExecute(translatedText);
            final String translatedPhrase = translatedText;               // translated phrase in desired language

            final String translationPhrase = allEnglishFromDB.get(updatingPosition);      // get each english word stored in the arrayList of all english words


            // update translations in db ----------

            // get one english phrase from db
            final EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

            final LiveData<EnglishEntered> englishResultObservable = englishRepository.getEnglishByEnglish(translationPhrase);

            englishResultObservable.observe(Dictionary.this, new Observer<EnglishEntered>() {
                @Override
                public void onChanged(EnglishEntered englishEntered) {
//                    System.out.println(englishEntered.getEnglish());

//                    need to use switch statement here to place the update in the proper column of the table>>>>>>>>>>>>>>
//                    System.out.println(translatedPhrase+"```````````");
                    englishEntered.setTranslationLang0(translatedPhrase);          // text to be changed
//                    System.out.println(englishEntered.getTranslationLang0());

                    allTranslationsOfChosen.add(updatingPosition,translatedPhrase);     // updating temporary arraylist to display

//                    System.out.println(allTranslationsOfChosen);

                    englishRepository.updateTask(englishEntered);       // update record

                    englishResultObservable.removeObserver(this);           // to stop retrieving the result repeatedly after getting it once

                }
            });

            updatingPosition++;
        }
    }

    //-------------------

    private void updateTranslation(String translationLang, String translationText) {
        // get the translation code of the chosen language
        translationLanguageCode = languageCodes.get(translationLang);

        // translate using Watson Translator
        translationService = initLanguageTranslatorService();           // connect & initiate to the cloud translation service
        new TranslationTask().execute(translationText, translationLanguageCode);
    }
}
