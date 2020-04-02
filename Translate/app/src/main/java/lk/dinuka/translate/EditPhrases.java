package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import javax.annotation.Nullable;

import lk.dinuka.translate.databases.EnglishEntered;
import lk.dinuka.translate.databases.EnglishRepository;
import lk.dinuka.translate.util.AppUtils;
import lk.dinuka.translate.util.MyDisplayAdapter;
import lk.dinuka.translate.util.MyEditAdapter;

public class EditPhrases extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private EditText chosenText;
    String chosenPhrase;             // stores the chosen English word/ phrase to be edited

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);

        chosenText = findViewById(R.id.editText_plainText);

        // get all English phrases from db and display
        recyclerView = findViewById(R.id.edit_recycler_view);


        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // specify the adapter (a bridge between a UI component and a data source)
        mAdapter = new MyEditAdapter(MainActivity.allEnglishFromDB);          // insert list of words here
        recyclerView.setAdapter(mAdapter);


    }

    public void chooseEnglish(View view) {
//        chosenText.setVisibility(View.VISIBLE);


        //display chosen english in edit text box <- only if a phrase/ word is chosen
        if (chosenPhrase!=null){

        }

    }

    public void updateAndSaveEnglish(View view) {

        // X - remove old english phrase from db
        // X - insert new phrase into db
        // update english in db
        EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());
//        EnglishEntered englishEntered = englishRepository.getEnglishByID(2);

// CAN'T GET DATABASE ENTRY OBJECT FROM ID>>>>>>>>>>>>>>?????????????????!!!!!!!!


        // This creates a new phrase and adds it into the position. It replaces the previous record
        // created_at is null

        EnglishEntered englishEntered = new EnglishEntered();
        englishEntered.setId(2);                // get this ID from the chosen one

//        englishEntered.setEnglish("Hello World!");          // text to be changed
//        englishRepository.updateTask(englishEntered);


        // refresh page with new info ------------
    }
}
