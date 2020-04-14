package lk.dinuka.translate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.dinuka.translate.databases.foreign.ForeignLanguage;
import lk.dinuka.translate.databases.foreign.ForeignRepository;
import lk.dinuka.translate.services.MyLanguageAdapter;

import static lk.dinuka.translate.MainActivity.foreignLanguageSubs;
import static lk.dinuka.translate.MainActivity.languageCodes;


public class LanguageSubscription extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private LanguageTranslator translationService;          // translation service

    public static HashMap<String, Boolean> foreignSubChanges = new HashMap<>();        // holds changes in Foreign subscriptions (languages that have been clicked by the user)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);

        // get and display all foreign languages stored in a separate entity of the db (ForeignLanguage)
        // with boolean value of subscribed

        // Receive all translatable languages using Watson Translator - [Needs to be done only if there was a change/addition in translatable languages]
        translationService = initLanguageTranslatorService();           // connect & initiate to the cloud translation service
        new LanguageSubscription.ReceiveIdentifiableLanguagesFromAPI().execute();


        // ---------------------------------

        recyclerView = findViewById(R.id.language_sub_recycler_view);


        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        List<String> allForeignLanguages = new ArrayList<>();


        for (Map.Entry<String, Boolean> entry : foreignLanguageSubs.entrySet()) {         //checking for all HashMap entries
            allForeignLanguages.add(entry.getKey());              //adding language name into allForeignLanguages arrayList
        }

        Collections.sort(allForeignLanguages);      // sort all languages in alphabetical order (because HashMap has no order)

        // specify the adapter (a bridge between a UI component and a data source)
        mAdapter = new MyLanguageAdapter(allForeignLanguages);          // insert list of languages
        recyclerView.setAdapter(mAdapter);

        // ---------------------------------

        if (savedInstanceState != null) {

            ArrayList<String> foreignLangs = new ArrayList<>();
            boolean[] foreignSubs = new boolean[foreignSubChanges.size()];

            foreignLangs = savedInstanceState.getStringArrayList("lang_changes");
            foreignSubs = savedInstanceState.getBooleanArray("subs_changes");

            if (foreignLangs != null) {
                if (foreignSubs != null) {      // avoiding null pointer exceptions
                    for (int i = 0; i < foreignLangs.size(); i++) {
                        foreignSubChanges.put(foreignLangs.get(i), foreignSubs[i]);
                    }
                }
            }
//            System.out.println(foreignSubChanges);
            mAdapter.notifyDataSetChanged();
            // pass in this arraylist into the constructor of the adapter as well>>>>>???
        }
    }


    public void updateLanguages(View view) {            // update database with new subscription status
        // save boolean statuses of all changed languages
        // take new changes into a HashMap and change only those in the db
//        System.out.println(foreignSubChanges);          // records the languages that the user has clicked in language adapter

        if (foreignSubChanges.size() > 0) {
            for (Map.Entry<String, Boolean> entry : foreignLanguageSubs.entrySet()) {         //checking for all HashMap entries

                if (foreignSubChanges.containsKey(entry.getKey())) {     // if a language name is in the changes arrayList, subscription has been changed

                    final Boolean subscription = foreignSubChanges.get(entry.getKey());

//                    if (subscription != (entry.getValue())) {       // update only if the subscription status is different.

                    // get this record from the db ---

                    final ForeignRepository foreignRepository = new ForeignRepository(getApplicationContext());

                    final LiveData<ForeignLanguage> foreignResultObservable = foreignRepository.getLangByName(entry.getKey());

                    foreignResultObservable.observe(this, new Observer<ForeignLanguage>() {
                        @Override
                        public void onChanged(ForeignLanguage foreignLanguage) {
//                            System.out.println(foreignLanguage.getLang_id());     // to check whether all the data was received
//                            System.out.println(foreignLanguage.getLanguage());
//                            System.out.println(foreignLanguage.getLanguageCode());
//                            System.out.println(foreignLanguage.getSubscriptionStatus());
//                            System.out.println(foreignLanguage.getCreatedAt());
//                            System.out.println(foreignLanguage.getUpdatedAt());

                            foreignLanguage.setSubscriptionStatus(subscription);       // update subscription status

                            foreignRepository.updateTask(foreignLanguage);          // update foreign language subscription status

                            foreignLanguageSubs.put(foreignLanguage.getLanguage(), subscription);       // update HashMap storing subscription statuses

                            foreignResultObservable.removeObserver(this);           // to stop retrieving the result repeatedly after getting it once
                        }

                    });
//                    }
                }
            }
            displayToast("The new subscriptions have been saved.");

        } else {
            displayToast("No changes have been made to subscriptions.");
        }
    }

    // --------------------------------------------------------------------
    public void displayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(Color.parseColor("#56ccf2"), PorterDuff.Mode.SRC_IN);

        toast.show();
    }

    private class ReceiveIdentifiableLanguagesFromAPI extends AsyncTask<String, Void, String> {     // get all available languages form the API at the beginning

        @Override
        protected String doInBackground(String... strings) {
            IdentifiableLanguages languages = translationService.listIdentifiableLanguages()
                    .execute().getResult();

//            System.out.println(languages);          // to check whether all languages were received

            for (int i = 0; i < languages.getLanguages().size(); i++) {               // get each language code & name separately
                String langName = languages.getLanguages().get(i).getName();      // language name
                String langCode = languages.getLanguages().get(i).getLanguage();      // language code

//                System.out.println(langName+": "+langCode);     // to check

                if (!languageCodes.containsKey(langName)) {
                    if (isNetworkAvailable()) {
                        // add each of these into the entity ForeignLanguage of the database
                        ForeignRepository foreignRepository = new ForeignRepository(getApplicationContext());
                        foreignRepository.insertTask(langName, langCode);

                        languageCodes.put(langName, langCode);       // saving lang names and codes in HashMap
                    } else {
                        displayToast("An internet connection is required to import the languages.");
                    }
                }
                // extra modification---
                // store all the language names/ codes in an arrayList -> check if the language exists in the system db and add the languages
                // only if not available in the db
            }
            return null;
        }

    }

    private LanguageTranslator initLanguageTranslatorService() {           // connect & initiate to the cloud translation service
        IamAuthenticator authenticator = new IamAuthenticator("2daMreRDE8V5zPRO3enCVHGUCH1sQJs-Kdq8ryPn4-ij");

        translationService = new LanguageTranslator("2018-05-01", authenticator);

        translationService.setServiceUrl("https://api.us-south.language-translator.watson.cloud.ibm.com/instances/caf1b5bc-ff11-4271-96cf-93372088290d");

        return translationService;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<String> foreignLangs = new ArrayList<>();
        boolean[] foreignSubs = new boolean[foreignSubChanges.size()];

        // create 2 ArrayLists with foreignSubChanges HashMap values in same order
        for (Map.Entry<String, Boolean> entry : foreignSubChanges.entrySet()) {         //checking for all HashMap entries
            foreignLangs.add(entry.getKey());
            foreignSubs[foreignLangs.indexOf(entry.getKey())] = entry.getValue();
        }

        outState.putStringArrayList("lang_changes", foreignLangs);
        outState.putBooleanArray("subs_changes", foreignSubs);

//        System.out.println(foreignSubChanges);
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
