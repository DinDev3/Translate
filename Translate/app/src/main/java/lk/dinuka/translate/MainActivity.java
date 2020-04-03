package lk.dinuka.translate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lk.dinuka.translate.databases.english.EnglishEntered;
import lk.dinuka.translate.databases.english.EnglishRepository;
import lk.dinuka.translate.databases.foreign.ForeignLanguage;
import lk.dinuka.translate.databases.foreign.ForeignRepository;
//import lk.dinuka.translate.databases.foreign.ForeignRepository;


public class MainActivity extends AppCompatActivity {

    public static List<String> allEnglishFromDB = new ArrayList<>();        // holds all English data received from db
    public static HashMap<String, Boolean> foreignLanguageSubs = new HashMap<>();        // holds all Foreign subscriptions received from db with subscription status

    // language codes can be stored with the name of the foreign language in a different HashMap in the translate page.
//    public static HashMap<String, String> languageCodes = new HashMap<>();        // holds all Foreign language names with language codes
// can call for the language code from the db? will speed decrease then??---- needed in translation page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Opening of Activities using button clicks

//        add phrases button
        Button addPhrasesButton = findViewById(R.id.add_phrases_button);
        addPhrasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPhrases.class);
                startActivity(intent);
            }
        });

//        display phrases button
        Button displayPhrasesButton = findViewById(R.id.display_phrases_button);
        displayPhrasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayPhrases.class);
                startActivity(intent);
            }
        });

//        edit phrases button
        Button editPhrasesButton = findViewById(R.id.edit_phrases_button);
        editPhrasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditPhrases.class);
                startActivity(intent);
            }
        });

//        language subscription button
        Button langSubscriptionButton = findViewById(R.id.language_subscription_button);
        langSubscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LanguageSubscription.class);
                startActivity(intent);
            }
        });

//        translate button
        Button translateButton = findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TranslateActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        refreshData();      // reload updated data from db into allEnglishFromDB arrayList if there were any changes in data

    }


    public void refreshData() {
        //       --------------------------------------------------------------------------------------------
        // this is placed here so that whenever new words are added, when coming back to the Main Screen and
        // going to a display words screen, the list of words get updated.

        // get all english phrases from db and display
        EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

        englishRepository.getEnglishFromDB().observe(this, new Observer<List<EnglishEntered>>() {
            @Override
            public void onChanged(@Nullable List<EnglishEntered> allEnglish) {
                allEnglishFromDB.clear();           // clearing existing data
                for (EnglishEntered english : allEnglish) {
                    allEnglishFromDB.add(english.getEnglish());     // saving all english word/ phrases received

//                     can use these to check in console
//                    System.out.println("-----------------------");
//                    System.out.println(english.getId());
//                    System.out.println(english.getEnglish());
//                    System.out.println(english.getCreatedAt());
//                    System.out.println(english.getUpdatedAt());
                }
            }
        });

//        --------------------------------------------------------------------------------------------

        // get all foreign languages from db and display with subscription status
        // get all english phrases from db and display
        ForeignRepository foreignRepository = new ForeignRepository(getApplicationContext());

        foreignRepository.getLangsFromDB().observe(this, new Observer<List<ForeignLanguage>>() {
            @Override
            public void onChanged(@Nullable List<ForeignLanguage> allLangs) {
                foreignLanguageSubs.clear();           // clearing existing data
                for (ForeignLanguage language : allLangs) {
                    if (language.getSubscriptionStatus() != foreignLanguageSubs.get(language.getLanguage()))
                        foreignLanguageSubs.put(language.getLanguage(), language.getSubscriptionStatus());     // get the subscription status of the language saved in the db
                }
            }
        });
    }
}
