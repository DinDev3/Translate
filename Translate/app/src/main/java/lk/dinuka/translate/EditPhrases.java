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

import java.util.List;

import lk.dinuka.translate.databases.english.EnglishEntered;
import lk.dinuka.translate.databases.english.EnglishRepository;
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
        if (chosenPhrase != null) {

        }

    }

    public void updateAndSaveEnglish(View view) {
        // update english in db


        // get the ID passed in from the chosen one----------->>>>>



        // get one english phrase from db and display
        final EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

        final LiveData<EnglishEntered> englishResultObservable = englishRepository.getEnglishByID(1);

        englishResultObservable.observe(this, new Observer<EnglishEntered>() {
            @Override
            public void onChanged(EnglishEntered englishEntered) {
//                System.out.println(englishEntered.getId());     // to check whether all the data was received
//                System.out.println(englishEntered.getEnglish());
//                System.out.println(englishEntered.getCreatedAt());
//                System.out.println(englishEntered.getUpdatedAt());

                englishEntered.setEnglish("Hello World!");          // text to be changed

                englishRepository.updateTask(englishEntered);

                englishResultObservable.removeObserver(this);           // to stop retrieving the result repeatedly after getting it once
            }

        });


        // refresh page with new info ------------


    }
}
