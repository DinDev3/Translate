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


public class MainActivity extends AppCompatActivity {

    public static List<String> allEnglishFromDB = new ArrayList<>();        // holds all English data received from db
    public static HashMap<String, Boolean> foreignLanguageSubs = new HashMap<>();        // holds all Foreign subscriptions received from db with subscription status


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

        refreshData();      // load data from db into allEnglishFromDB arrayList

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

//                     can use these to check data of received records in console
//                    System.out.println("-----------------------1");
//                    System.out.println(english.getId());
//                    System.out.println(english.getEnglish());
//                    System.out.println(english.getCreatedAt());
//                    System.out.println(english.getUpdatedAt());
                }
            }
        });

//        --------------------------------------------------------------------------------------------

        // get all foreign languages from db. Extract language name & subscription status
        ForeignRepository foreignRepository = new ForeignRepository(getApplicationContext());

        foreignRepository.getLangsFromDB().observe(this, new Observer<List<ForeignLanguage>>() {
            @Override
            public void onChanged(@Nullable List<ForeignLanguage> allLangs) {
                foreignLanguageSubs.clear();           // clearing existing data
                for (ForeignLanguage language : allLangs) {
//                    System.out.println("-----------------------2");

                    if (language.getSubscriptionStatus() != foreignLanguageSubs.get(language.getLanguage()))
                        foreignLanguageSubs.put(language.getLanguage(), language.getSubscriptionStatus());     // get the subscription status of the language saved in the db
                }
            }
        });
    }
}
