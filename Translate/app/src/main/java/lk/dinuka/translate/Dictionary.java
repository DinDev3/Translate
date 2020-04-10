package lk.dinuka.translate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import lk.dinuka.translate.databases.english.EnglishEntered;
import lk.dinuka.translate.databases.english.EnglishRepository;

public class Dictionary extends AppCompatActivity {

    List<String> allTranslationsOfChosen = new ArrayList<>();           // stores all the translated words of the desired language,
                                                                        // to pass it into the recyclerview adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        receiveData();

        // open activity Dictionary Language Subscriptions List
        Button showLangListButton = findViewById(R.id.subscribe_button);
        showLangListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dictionary.this, DictionarySubscriptions.class);
                startActivity(intent);
            }
        });
    }


    private void receiveData() {        // used to display english phrase with one translation
        // get all english phrases from db and display
        EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

        englishRepository.getEnglishFromDB().observe(this, new Observer<List<EnglishEntered>>() {
            @Override
            public void onChanged(@Nullable List<EnglishEntered> allEnglish) {
                for (EnglishEntered english : allEnglish) {
//                     can use these to check data of received records in console
                    System.out.println(english.getEnglish());


//                    use switch case to receive the desired language here>>>>>>>>>>>>??????????????????????
//                    add received translations to allTranslationsOfChosen

                    System.out.println(english.getTranslationLang0());
                }
            }
        });
    }




}
