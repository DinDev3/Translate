package lk.dinuka.translate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import lk.dinuka.translate.databases.english.EnglishEntered;
import lk.dinuka.translate.databases.english.EnglishRepository;
import lk.dinuka.translate.services.MyDictionaryAdapter;

import static lk.dinuka.translate.MainActivity.allEnglishFromDB;

public class Dictionary extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyDictionaryAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    static List<String> allTranslationsOfChosen = new ArrayList<>();           // stores all the translated words of the desired language,

    // to pass it into the recyclerview adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

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
    }

    public void updateDisplay(View view) {      // update & display english phrases with translations

        if (allEnglishFromDB.size() > allTranslationsOfChosen.size()) {        // if new words have been added
            // update translations in db

        }

        receiveData();

    }


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

}
