package lk.dinuka.translate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

    // reference to SharedPreferences object
    private SharedPreferences mPreferences;
    // name of the sharedPrefFile
    private String mSharedPrefFile = "lk.dinuka.translate.translateLangs";

    // Keys to get all Languages from sharedPreferences
    final String LANG_ONE = "langOne";
    final String LANG_TWO = "langTwo";
    final String LANG_THREE = "langThree";
    final String LANG_FOUR = "langFour";
    final String LANG_FIVE = "langFive";


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

//    public static ArrayList<String> savedLanguages = new ArrayList<>();        // holds changes in saved languages (languages that have been clicked by the user)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_subscriptions);

        // ---------------------------------

        // initialize the shared preferences
        mPreferences = getSharedPreferences(mSharedPrefFile, MODE_PRIVATE);


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

    }


    //    - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


    public void updateSubscriptions(View view) {        // shared preferences are updated here - > as a result the arrayList of savedLanguages will be updated and give false
        // results if the db isn't updated
        int totalPossibleLanguages = 5;     // the db supports 5 savable translation languages for now

        int totalSavedLanguages = 0;                // total saved languages
        int totalRequiredToBeSaved = 0;             // newly added languages that haven't been saved before & language that have to be removed


        for (String langName :
                savedLanguages) {
            if (langName != null) {        // if null, no language
                totalSavedLanguages++;
            }
        }

        for (Map.Entry<String, Boolean> entry : savedLangChanges.entrySet()) {         //checking for all HashMap entries
            if (entry.getValue() && !(savedLanguages.contains(entry.getKey()))) {             // only languages that haven't been already saved will be counted
                totalRequiredToBeSaved++;
            } else if (!entry.getValue() && (savedLanguages.contains(entry.getKey()))) {     // saved languages that have been requested to be removed
                totalSavedLanguages--;

                totalPossibleLanguages++;       // otherwise, the removed position won't be counted as an available slot for another language if 5 langs were already there
                totalRequiredToBeSaved++;       // an update has been done
            }
        }

        int totalSavableChanges = totalPossibleLanguages - totalSavedLanguages;     // total languages that can be saved

        if (totalRequiredToBeSaved > 0) {
            if (totalSavedLanguages == 5) {
                displayToast("Currently, the app supports saving up to 5 languages. You have reached the limit of the number of languages that can be saved.");

            } else if (totalSavableChanges >= totalRequiredToBeSaved) {     // this will check whether any more languages can be added


                for (Map.Entry<String, Boolean> entry : savedLangChanges.entrySet()) {         //checking for all HashMap entries
                    if (entry.getValue() && (savedLanguages.contains(entry.getKey()))) {             // languages that have already been saved & requested to be saved will be ignored
//                        savedLangChanges.remove(entry.getKey());

                    } else if (!entry.getValue() && (savedLanguages.contains(entry.getKey()))) {     // saved languages that have been requested to be removed

                        // get shared preference position?
                        int sharedPrefPositionOfLang = savedLanguages.indexOf(entry.getKey());         // savedLanguages arrayList position = shared preference position

                        // make all values of the column null in db - - - >
                        removeTranslations(sharedPrefPositionOfLang);


                        // make position of shared preference null
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

                        switch (sharedPrefPositionOfLang) {
                            case 0:
                                preferencesEditor.putString(LANG_ONE, null);
                                break;
                            case 1:
                                preferencesEditor.putString(LANG_TWO, null);
                                break;
                            case 2:
                                preferencesEditor.putString(LANG_THREE, null);
                                break;
                            case 3:
                                preferencesEditor.putString(LANG_FOUR, null);
                                break;
                            case 4:
                                preferencesEditor.putString(LANG_FIVE, null);
                                break;
                        }
                        // .apply() saves the preferences asynchronously, off of the UI thread
                        preferencesEditor.apply();


                        // remove language from arrayList & temporary hashMap
//                        savedLangChanges.remove(entry.getKey());

                        savedLanguages.remove(sharedPrefPositionOfLang);
                        savedLanguages.add(sharedPrefPositionOfLang,null);          // to maintain the size of the arrayList

                        displayToast("Languages have been successfully updated.");


                    } else if (entry.getValue() && !(savedLanguages.contains(entry.getKey()))) {         // new languages that haven't been saved before
                        // since the availability of slots to save languages have been checked before, it's not necessary to check again here

                        // get available shared preference position -> has null value
                        int freePosition = 0;
                        for (int i = 0; i < savedLanguages.size(); i++){
                            if (savedLanguages.get(i) == null){     // finding the first available position to store a language
                                freePosition = i;
//                                break;        // can add to the first available column if uncommented
                            }
                        }

                            // add language name to shared preference position
                        String langNameToBeAdded = entry.getKey();

                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();

                        switch (freePosition) {
                            case 0:
                                preferencesEditor.putString(LANG_ONE, langNameToBeAdded);
                                break;
                            case 1:
                                preferencesEditor.putString(LANG_TWO, langNameToBeAdded);
                                break;
                            case 2:
                                preferencesEditor.putString(LANG_THREE, langNameToBeAdded);
                                break;
                            case 3:
                                preferencesEditor.putString(LANG_FOUR, langNameToBeAdded);
                                break;
                            case 4:
                                preferencesEditor.putString(LANG_FIVE, langNameToBeAdded);
                                break;
                        }
                        // .apply() saves the preferences asynchronously, off of the UI thread
                        preferencesEditor.apply();

                        //        commit() method to synchronously save the preferences. It's discouraged as it can block other operations.


                        // remove language from arrayList & temporary hashMap
//                        savedLangChanges.remove(entry.getKey());

                        savedLanguages.remove(freePosition);         // to maintain the size and desired positions of the arrayList

                        // add the newly added language to the savedLanguages list
                        savedLanguages.add(freePosition,langNameToBeAdded);


                        displayToast("Languages have been successfully updated.");

                    } else {
                        //"No changes were requested to be made to the Language Subscriptions of the Dictionary."
                        // not required to show a message here.
                    }

                }
                // clear savedLangChanges hashMap when all changes are done
                savedLangChanges.clear();


            } else {
                displayToast("Currently, the app supports saving up to 5 languages. You have chosen " + (totalRequiredToBeSaved - totalSavableChanges) + " language(s) more than the limit.");


            }
        } else {
            displayToast("No changes were requested to be made to the Language Subscriptions of the Dictionary.");
        }

        // check if savedLangChanges.get() = true and existing in savedLanguages ArrayList. No need to make any changes then
    }

    public void displayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(Color.parseColor("#56ccf2"), PorterDuff.Mode.SRC_IN);

        toast.show();
    }


//    - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void removeTranslations(final int selectedLanguagePosition) {         // used to remove all translations

        // get all english phrases from db and display
        final EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

        englishRepository.getEnglishFromDB().observe(this, new Observer<List<EnglishEntered>>() {
            @Override
            public void onChanged(@Nullable List<EnglishEntered> allEnglish) {
                allTranslationsOfChosen.clear();            // clearing existing data
                for (EnglishEntered english : allEnglish) {                             // clear all translations of specified language

//                    use switch case to choose the desired language here
                    switch (selectedLanguagePosition) {
                        case 0:
                            english.setTranslationLang0(null);
                            break;
                        case 1:
                            english.setTranslationLang1(null);
                            break;
                        case 2:
                            english.setTranslationLang2(null);
                            break;
                        case 3:
                            english.setTranslationLang3(null);
                            break;
                        case 4:
                            english.setTranslationLang4(null);
                            break;
                    }
                    englishRepository.updateTask(english);       // update record

                }
            }
        });

    }


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
////           pass in translationLang here>>>>>>>>
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
//                    englishEntered.setTranslationLang0(null);          // text to be changed
////                    System.out.println(englishEntered.getTranslationLang0());
//
//                    englishRepository.updateTask(englishEntered);       // update record
//
//                    englishResultObservable.removeObserver(this);           // to stop retrieving the result repeatedly after getting it once
//
//                }
//            });
//
//        }
//    }
//
//    public void translateEnglishPhrase(String translationLang) {         // Translates and saves text onClick of Update subscriptions button
//        // get the translation code of the chosen language
//        translationLanguageCode = languageCodes.get(translationLang);
//
//        // translate using Watson Translator
//        translationService = initLanguageTranslatorService();           // connect & initiate to the cloud translation service
//        new TranslationTask().execute(null, translationLanguageCode);
//    }

    //    - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

}
