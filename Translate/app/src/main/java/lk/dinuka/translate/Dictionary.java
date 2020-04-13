package lk.dinuka.translate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.dinuka.translate.databases.english.EnglishEntered;
import lk.dinuka.translate.databases.english.EnglishRepository;
import lk.dinuka.translate.services.MyDictionaryAdapter;

import static lk.dinuka.translate.MainActivity.allEnglishFromDB;
import static lk.dinuka.translate.MainActivity.languageCodes;

public class Dictionary extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerView;
    private MyDictionaryAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public String selectedSpinnerLanguage;         // holds the translation language Name chosen from the spinner

    private LanguageTranslator translationService;          // translation service

    private String translationLang;                 // Chosen language to translate phrases into
    private String translationLanguageCode;             // used to pass in the translation code of the chosen language
    private String translationText;                     // The English text that is required to be translated will be held in this variable
    private String translatedText;              // The translated text in the desired language

    private int updatingPosition;               // used to get the position of the phrase that's being translated, to update

    static List<String> allTranslationsOfChosen = new ArrayList<>();           // stores all the translated words of the desired language,
    public static ArrayList<String> savedLanguages = new ArrayList<>();        // holds changes in saved languages (languages that have been clicked by the user)
    public static HashMap<String, Boolean> savedLangChanges = new HashMap<>();        // holds changes in saved languages (languages that have been clicked by the user)


    // reference to SharedPreferences object
    private SharedPreferences mPreferences;
    // name of the sharedPrefFile
    private String sharedPrefFile = "lk.dinuka.translate.translateLangs";

    // Keys to get all Languages from sharedPreferences
    final String LANG_ONE = "langOne";
    final String LANG_TWO = "langTwo";
    final String LANG_THREE = "langThree";
    final String LANG_FOUR = "langFour";
    final String LANG_FIVE = "langFive";

    // Translation language names that have all translations saved in the db
    private String mTranslationLangOne;
    private String mTranslationLangTwo;
    private String mTranslationLangThree;
    private String mTranslationLangFour;
    private String mTranslationLangFive;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);


        // use shared preferences to get saved order of translations languages columns in db

        // initialize the shared preferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        mTranslationLangOne = mPreferences.getString(LANG_ONE, null);
        mTranslationLangTwo = mPreferences.getString(LANG_TWO, null);
        mTranslationLangThree = mPreferences.getString(LANG_THREE, null);
        mTranslationLangFour = mPreferences.getString(LANG_FOUR, null);
        mTranslationLangFive = mPreferences.getString(LANG_FIVE, null);
//        getString() method takes two arguments: one for the key, and
//        the other for the default value if the key cannot be found


        if (savedLanguages.isEmpty()) {         // otherwise gets added multiple times when the activity is reopened
            savedLanguages.add(mTranslationLangOne);
            savedLanguages.add(mTranslationLangTwo);
            savedLanguages.add(mTranslationLangThree);
            savedLanguages.add(mTranslationLangFour);
            savedLanguages.add(mTranslationLangFive);
        }


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

        // ----------Recycler view adapter

        recyclerView = findViewById(R.id.display_recycler_view);
        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify the adapter (a bridge between a UI component and a data source)
        mAdapter = new MyDictionaryAdapter(allEnglishFromDB, allTranslationsOfChosen);          // insert initial list of words here
        recyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

        //---------Spinner
        ArrayList<String> spinnerLanguages = new ArrayList<>();

        for (String langName :
                savedLanguages) {
            if (langName!=null){        // if null, arrayAdapter of spinner gives null pointer
                spinnerLanguages.add(langName);
            }
        }


        // create the spinner
        Spinner spinner = findViewById(R.id.dic_language_spinner);

        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        // Create ArrayAdapter using the string array and default spinner layout.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerLanguages);


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
        selectedSpinnerLanguage = spinnerLabel;             // this is the language chosen by the user
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    public void displayRecords(View view) {      // display english phrases with translations
        receiveData(selectedSpinnerLanguage);

        TextView mTranslateColumnLabel = findViewById(R.id.translated_text);
        mTranslateColumnLabel.setText(selectedSpinnerLanguage);
    }


    public void updateRecords(View view) {      // update translations of newly added english phrases into the db
        translationLang = selectedSpinnerLanguage;
        updatingPosition = 0;       // resetting update position


        for (int i = 0; i < allEnglishFromDB.size(); i++) {
            updateTranslation(translationLang, allEnglishFromDB.get(i));            // update translations in db
        }

//        System.out.println("**"+allTranslationsOfChosen);
    }


    // ----------------------
    public void receiveData(final String selectedSpinnerLanguage) {        // used to display english phrase with one translation
        // get all english phrases from db and display
        EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

        englishRepository.getEnglishFromDB().observe(this, new Observer<List<EnglishEntered>>() {
            @Override
            public void onChanged(@Nullable List<EnglishEntered> allEnglish) {
                allTranslationsOfChosen.clear();            // clearing existing data
                for (EnglishEntered english : allEnglish) {

//                    add received translations to allTranslationsOfChosen

                    int translationLangPosition = savedLanguages.indexOf(selectedSpinnerLanguage);

//                    use switch case to receive the desired language here
                    switch (translationLangPosition) {
                        case 0:
                            if (english.getTranslationLang0() != null) {
                                allTranslationsOfChosen.add(english.getTranslationLang0());
                            } else {
                                allTranslationsOfChosen.add(null);
                            }
                            break;
                        case 1:
                            if (english.getTranslationLang1() != null) {
                                allTranslationsOfChosen.add(english.getTranslationLang1());
                            } else {
                                allTranslationsOfChosen.add(null);
                            }
                            break;
                        case 2:
                            if (english.getTranslationLang2() != null) {
                                allTranslationsOfChosen.add(english.getTranslationLang2());
                            } else {
                                allTranslationsOfChosen.add(null);
                            }
                            break;
                        case 3:
                            if (english.getTranslationLang3() != null) {
                                allTranslationsOfChosen.add(english.getTranslationLang3());
                            } else {
                                allTranslationsOfChosen.add(null);
                            }
                            break;
                        case 4:
                            if (english.getTranslationLang4() != null) {
                                allTranslationsOfChosen.add(english.getTranslationLang4());
                            } else {
                                allTranslationsOfChosen.add(null);
                            }
                            break;
                    }
//                    System.out.println(allTranslationsOfChosen);
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

                    int translationLangPosition = savedLanguages.indexOf(translationLang);
                    switch (translationLangPosition) {
                        case 0:
                            englishEntered.setTranslationLang0(translatedPhrase);          // text to be changed
//                            System.out.println(englishEntered.getTranslationLang0());
                            break;
                        case 1:
                            englishEntered.setTranslationLang1(translatedPhrase);          // text to be changed
//                            System.out.println(englishEntered.getTranslationLang1());
                            break;
                        case 2:
                            englishEntered.setTranslationLang2(translatedPhrase);          // text to be changed
//                            System.out.println(englishEntered.getTranslationLang2());
                            break;
                        case 3:
                            englishEntered.setTranslationLang3(translatedPhrase);          // text to be changed
//                            System.out.println(englishEntered.getTranslationLang3());
                            break;
                        case 4:
                            englishEntered.setTranslationLang4(translatedPhrase);          // text to be changed
//                            System.out.println(englishEntered.getTranslationLang4());
                            break;
                    }

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
