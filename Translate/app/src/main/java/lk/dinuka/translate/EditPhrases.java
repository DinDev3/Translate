package lk.dinuka.translate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.annotations.Since;

import java.util.List;

import lk.dinuka.translate.databases.english.EnglishEntered;
import lk.dinuka.translate.databases.english.EnglishRepository;
import lk.dinuka.translate.util.MyEditAdapter;

import static lk.dinuka.translate.MainActivity.allEnglishFromDB;

public class EditPhrases extends AppCompatActivity implements MyEditAdapter.OnEditAdapterListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private EditText chosenEditText;
    String chosenPhrase;             // stores the chosen English word/ phrase to be edited
    //    int chosenIDInDB;             // id of the chosenPhrase in the database
    Boolean isEdit = false;                 // used to check whether the edit button has been pressed at least once

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);

        chosenEditText = findViewById(R.id.editText_plainText);


        // get all English phrases from db and display
        recyclerView = findViewById(R.id.edit_recycler_view);


        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // specify the adapter (a bridge between a UI component and a data source)
        mAdapter = new MyEditAdapter(allEnglishFromDB, this);          // insert list of words here
        recyclerView.setAdapter(mAdapter);


    }

    public void chooseEnglish(View view) {
//        chosenEditText.setVisibility(View.VISIBLE);

        //display chosen english in edit text box <- only if a phrase/ word is chosen
        if (chosenPhrase != null) {
            isEdit = true;          // edit button pressed at least once

            chosenEditText.setText(chosenPhrase);
        } else {
            displayToast("Choose a word/ phrase to be translated");
        }

    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    public void updateAndSaveEnglish(View view) {
        // update english in db

        //get change in text from text currently in EditText box
        final String updatedPhrase = chosenEditText.getText().toString();
        System.out.println(updatedPhrase);            // to test

        if (updatedPhrase.length() != 0) {              // The phrase/ word has to be replaced with another. Can't be emptied

            // get one english phrase from db and display
            final EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

            final LiveData<EnglishEntered> englishResultObservable = englishRepository.getEnglishByEnglish(chosenPhrase);

            englishResultObservable.observe(this, new Observer<EnglishEntered>() {
                @Override
                public void onChanged(EnglishEntered englishEntered) {
                    System.out.println(englishEntered.getId());     // to check whether all the data was received
                    System.out.println(englishEntered.getEnglish());
                    System.out.println(englishEntered.getCreatedAt());
                    System.out.println(englishEntered.getUpdatedAt());

                    englishEntered.setEnglish(updatedPhrase);          // text to be changed

                    englishRepository.updateTask(englishEntered);       // update record

                    englishResultObservable.removeObserver(this);           // to stop retrieving the result repeatedly after getting it once
                }

            });

        } else {
            displayToast("The phrase must have at least one character");
        }

        // refresh page with new info ------------
//        refreshData();              // refresh the data----- refreshing happens before update is over. Old record comes back in
        // Therefore, non existant in db the next time


    }

    @Override
    public void onEnglishClick(int position) { // get English word/ phrase to be translated - onClick of a recyclerView view holder
        chosenPhrase = allEnglishFromDB.get(position);           // same position as in the Adapter
//        System.out.println(chosenPhrase);          // translation text

        // this is the position in the recyclerview. These records are queried in alphabetical order.
        // Therefore, can't get the id in the db from here

        if (isEdit) {
            chosenEditText.setText(chosenPhrase);           // display chosen text in plainTextView
        }
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
                    System.out.println("-----------------------");
                    System.out.println(english.getId());
                    System.out.println(english.getEnglish());
                    System.out.println(english.getCreatedAt());
                    System.out.println(english.getUpdatedAt());
                }
            }
        });


//        mAdapter.notifyDataSetChanged();      // get the adapter to show changes
    }
}
