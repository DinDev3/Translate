package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

import com.ibm.cloud.sdk.core.http.ServiceCall;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguage;
import com.ibm.watson.language_translator.v3.model.IdentifiableLanguages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lk.dinuka.translate.databases.foreign.ForeignRepository;
import lk.dinuka.translate.util.MyLanguageAdapter;

public class LanguageSubscription extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private LanguageTranslator translationService;          // translation service


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);

        // get and display all foreign languages stored in a separate entity of the db (ForeignLanguage)
        // with boolean value of subscribed>>>>>>>>>>>>>>>>>>>>>>>>


        // Receive all translatable languages using Watson Translator - [Needs to be done only if there was a change/addition in translatable languages]
//        translationService = initLanguageTranslatorService();           // connect & initiate to the cloud translation service
//        new LanguageSubscription.ReceiveIdentifiableLanguages().execute();


        recyclerView = findViewById(R.id.language_sub_recycler_view);


        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        List<String> allForeignLanguages = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : MainActivity.foreignLanguageSubs.entrySet()) {         //checking for all HashMap entries
            allForeignLanguages.add(entry.getKey());              //adding value of selected key into playerNames arrayList
        }


        // specify the adapter (a bridge between a UI component and a data source)
        mAdapter = new MyLanguageAdapter(allForeignLanguages);          // insert list of languages
        recyclerView.setAdapter(mAdapter);

    }


    public void updateLanguages(View view) {
        // save boolean statuses of all changed languages
        // take new changes into a HashMap and change only those in the db?

    }

    // --------------------------------------------------------------------

    private class ReceiveIdentifiableLanguages extends AsyncTask<String, Void, String> {     // get all available languages form the API at the beginning

        @Override
        protected String doInBackground(String... strings) {
            IdentifiableLanguages languages = translationService.listIdentifiableLanguages()
                    .execute().getResult();

//            System.out.println(languages);          // to check whether all languages were received

            for (int i = 0; i < languages.getLanguages().size(); i++) {               // get each language code & name separately
                String langName = languages.getLanguages().get(i).getName();      // language name
                String langCode = languages.getLanguages().get(i).getLanguage();      // language code

//                System.out.println(langName+": "+langCode);     // to check

                // add each of these into the entity ForeignLanguage of the database
                ForeignRepository foreignRepository = new ForeignRepository(getApplicationContext());
                foreignRepository.insertTask(langName, langCode);

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
}
