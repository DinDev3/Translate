package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import lk.dinuka.translate.services.MyDicSubsAdapter;
import lk.dinuka.translate.services.MyLanguageAdapter;

import static lk.dinuka.translate.Dictionary.allTranslationsOfChosen;
import static lk.dinuka.translate.Dictionary.savedLangChanges;
import static lk.dinuka.translate.Dictionary.savedLanguages;
import static lk.dinuka.translate.MainActivity.allEnglishFromDB;
import static lk.dinuka.translate.MainActivity.foreignLanguageSubs;
import static lk.dinuka.translate.MainActivity.languageCodes;

public class DictionarySubscriptions extends AppCompatActivity {
    private LanguageTranslator translationService;          // translation service

    private String translationLang;                 // Chosen language to translate phrases into
    private String translationLanguageCode;             // used to pass in the translation code of the chosen language
    private String translationText;                     // The English text that is required to be translated will be held in this variable
    private String translatedText;              // The translated text in the desired language

    private int updatingPosition;               // used to get the position of the phrase that's being translated, to update


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

//    public static ArrayList<String> savedLanguages = new ArrayList<>();        // holds changes in saved languages (languages that have been clicked by the user)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_subscriptions);

        // get and display all foreign languages stored in a separate entity of the db (ForeignLanguage)
        // with boolean value of subscribed

        // ---------------------------------

        recyclerView = findViewById(R.id.language_sub_recycler_view);


        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        List<String> availableLanguages = new ArrayList<>();


        for (Map.Entry<String, Boolean> entry : foreignLanguageSubs.entrySet()) {         //checking for all HashMap entries
            if (entry.getValue() == true) {             // only subscribed languages will be shown here to choose
                availableLanguages.add(entry.getKey());              //adding language name into availableLanguages arrayList
            }
        }

        Collections.sort(availableLanguages);      // sort all languages in alphabetical order (because HashMap has no order)

        // specify the adapter (a bridge between a UI component and a data source)
        mAdapter = new MyDicSubsAdapter(availableLanguages);          // insert list of languages
        recyclerView.setAdapter(mAdapter);

        // ---------------------------------
//
//        if (savedInstanceState != null) {     // shared preferences can take care of this
//
//            ArrayList<String> foreignLangs = new ArrayList<>();
//            boolean[] foreignSubs = new boolean[savedLangChanges.size()];
//
//            foreignLangs = savedInstanceState.getStringArrayList("lang_changes");
//            foreignSubs = savedInstanceState.getBooleanArray("subs_changes");
//
//            if (foreignLangs != null) {
//                if (foreignSubs != null) {      // avoiding null pointer exceptions
//                    for (int i = 0; i < foreignLangs.size(); i++) {
//                        savedLangChanges.put(foreignLangs.get(i), foreignSubs[i]);
//                    }
//                }
//            }
////            System.out.println(foreignSubChanges);
////            mAdapter.notifyDataSetChanged();
//            // pass in this arraylist into the constructor of the adapter as well>>>>>???
//        }


    }


    //    - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    public void updateSubscriptions(View view) {        // shared preferences are updated here - > as a result the arrayList of savedLanguages will be updated and give false
        // results if the db isn't updated
        int totalPossibleLanguages = 5;     // the db supports 5 savable translation languages for now

        int totalSavedLanguages = 0;                // total saved languages
        int totalRequiredToBeSaved = 0;             // newly added languages that haven't been saved before


        for (String langName :
                savedLanguages) {
            if (langName != null) {        // if null, no language
                totalSavedLanguages++;
            }
        }

        for (Map.Entry<String, Boolean> entry : savedLangChanges.entrySet()) {         //checking for all HashMap entries
            if ((entry.getValue() == true) && !(savedLanguages.contains(entry.getKey()))) {             // only saved languages that haven't been already saved will be counted
                totalRequiredToBeSaved++;
            }
        }

        int totalSavableChanges = totalPossibleLanguages - totalSavedLanguages;     // total languages that can be saved

        if (totalSavableChanges >= totalRequiredToBeSaved) {     // this will check whether any more languages can be added
            displayToast("~~~~~~~~~~~~~~~~~~~updating of languages should be done successfully~~~~~~~~~~~~~~~~~~~~~~~~");


        } else{
            displayToast("Currently, the app supports saving up to 5 languages. You have chosen "+(totalRequiredToBeSaved - totalSavableChanges)+" language(s) more than the limit.");


        }

        // check if savedLangChanges.get() = true and exisiting in savedLanguuages ArrayList. No need to make any changes then
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
//    - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
//
//    private void saveTranslations() {
//
//        for (int n = 0; n < savedLanguages.size(); n++) {        // update translations of all translation languages
//            if (savedLanguages.get(n) != null) {                // a translation language must exist to translate (null values can be there in this arrayList)
//
//                updatingPosition = 0;       // resetting update position
//                allTranslationsOfChosen.clear();        //resetting translations arrayList
//                for (int i = 0; i < allEnglishFromDB.size(); i++) {
//
//                    translationText = allEnglishFromDB.get(i);      // get each english word stored in the arrayList of all english words
//
////           pass in translationLang here>>>>>>>>>>>>>>>>>>>>>>>>>
//                    translationLang = savedLanguages.get(n);
//
//                    translateEnglishPhrase(translationLang);
////                    translateEnglishPhrase("Spanish");
//                }
//            }
//
//        }
//    }
//
//
//    //-------------------
//
//    private LanguageTranslator initLanguageTranslatorService() {           // connect & initiate to the cloud translation service
//        Authenticator authenticator = new IamAuthenticator("2daMreRDE8V5zPRO3enCVHGUCH1sQJs-Kdq8ryPn4-ij");
//
//        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);
//
//        service.setServiceUrl("https://api.us-south.language-translator.watson.cloud.ibm.com/instances/caf1b5bc-ff11-4271-96cf-93372088290d");
//
//        return service;
//    }
//
//
//    private class TranslationTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            TranslateOptions translateOptions = new TranslateOptions.Builder()
//                    .addText(params[0])
//                    .source(Language.ENGLISH)
//                    .target(params[1])    // pass in translationLanguage here, to get required language
//                    .build();
//
//            TranslationResult result = translationService.translate(translateOptions).execute().getResult();
//
//            String firstTranslation = result.getTranslations().get(0).getTranslation();
//
//            return firstTranslation;
//        }
//
//        @Override
//        protected void onPostExecute(String translatedText) {       // update one phrase at a time
//            super.onPostExecute(translatedText);
//            final String translatedPhrase = translatedText;               // translated phrase in desired language
//
//            final String translationPhrase = allEnglishFromDB.get(updatingPosition);      // get each english word stored in the arrayList of all english words
//            updatingPosition++;
//
//
//            // update translations in db ----------
//
//            // get one english phrase from db
//            final EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());
//
//            final LiveData<EnglishEntered> englishResultObservable = englishRepository.getEnglishByEnglish(translationPhrase);
//
//            englishResultObservable.observe(DictionarySubscriptions.this, new Observer<EnglishEntered>() {
//                @Override
//                public void onChanged(EnglishEntered englishEntered) {
////                    System.out.println(englishEntered.getEnglish());
//
////                    System.out.println(translatedPhrase+"```````````");
//                    englishEntered.setTranslationLang0(translatedPhrase);          // text to be changed
////                    System.out.println(englishEntered.getTranslationLang0());
//
//                    englishRepository.updateTask(englishEntered);       // update record
//
//                    englishResultObservable.removeObserver(this);           // to stop retrieving the result repeatedly after getting it once
//
//                }
//            });
//            System.out.println("~~~        updating complete       ~~~");
//        }
//    }
//
//    public void translateEnglishPhrase(String translationLang) {         // Translates and saves text onClick of Update subscriptions button
//        // get the translation code of the chosen language
//        translationLanguageCode = languageCodes.get(translationLang);
//
//        // translate using Watson Translator
//        translationService = initLanguageTranslatorService();           // connect & initiate to the cloud translation service
//        new TranslationTask().execute(translationText, translationLanguageCode);
//    }
//
//    //    - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

}
