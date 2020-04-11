package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;

import lk.dinuka.translate.databases.english.EnglishEntered;
import lk.dinuka.translate.databases.english.EnglishRepository;

import static lk.dinuka.translate.Dictionary.allTranslationsOfChosen;
import static lk.dinuka.translate.MainActivity.allEnglishFromDB;
import static lk.dinuka.translate.MainActivity.languageCodes;

public class DictionarySubscriptions extends AppCompatActivity {
    private LanguageTranslator translationService;          // translation service

    private String translationLanguageCode;             // used to pass in the translation code of the chosen language
    private String translationText;                     // The English text that is required to be translated will be held in this variable
    private String translatedText;              // The translated text in the desired language

    private int updatingPosition;               // used to get the position of the phrase that's being translated, to update

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_subscriptions);
    }

    public void updateSubscriptions(View view) {
        saveTranslations();
    }

//    - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void saveTranslations() {

        updatingPosition = 0;       // resetting update position
        allTranslationsOfChosen.clear();        //resetting translations arrayList
        for (int i = 0; i < allEnglishFromDB.size(); i++) {

            translationText = allEnglishFromDB.get(i);      // get each english word stored in the arrayList of all english words

//           pass in translationLang here>>>>>>>>>>>>>>>>>>>>>>>>>
//            translatedText = translateEnglishPhrase(translationLang);
            translateEnglishPhrase("Spanish");
        }
    }


    //-------------------

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
            updatingPosition++;


            // update translations in db ----------

            // get one english phrase from db
            final EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

            final LiveData<EnglishEntered> englishResultObservable = englishRepository.getEnglishByEnglish(translationPhrase);

            englishResultObservable.observe(DictionarySubscriptions.this, new Observer<EnglishEntered>() {
                @Override
                public void onChanged(EnglishEntered englishEntered) {
//                    System.out.println(englishEntered.getEnglish());

//                    System.out.println(translatedPhrase+"```````````");
                    englishEntered.setTranslationLang0(translatedPhrase);          // text to be changed
//                    System.out.println(englishEntered.getTranslationLang0());

                    englishRepository.updateTask(englishEntered);       // update record


//                    >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> check this  --- This should be done only when retrieving records

//                    if (allTranslationsOfChosen.size() < allEnglishFromDB.size()) {
//                        allTranslationsOfChosen.add(translatedPhrase);       // update temporary arraylist
//                    }

                    englishResultObservable.removeObserver(this);           // to stop retrieving the result repeatedly after getting it once

                }
            });

        }
    }

    public void translateEnglishPhrase(String translationLang) {         // Translates and displays text onClick of Translate button
        // get the translation code of the chosen language
        translationLanguageCode = languageCodes.get(translationLang);

        // translate using Watson Translator
        translationService = initLanguageTranslatorService();           // connect & initiate to the cloud translation service
        new TranslationTask().execute(translationText, translationLanguageCode);
    }
}
