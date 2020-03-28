package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import lk.dinuka.translate.util.MyDisplayAdapter;
import lk.dinuka.translate.util.MyEditAdapter;

public class EditPhrases extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);

        // get all English phrases from db and display
        recyclerView = findViewById(R.id.edit_recycler_view);


        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // specify the adapter (a bridge between a UI component and a data source)
//        mAdapter = new MyEditAdapter(MainActivity.allEnglishFromDB);          // insert list of words here

        // change mAdapter >>>>>> create another adapter class to handle this------->>>>>>>

        recyclerView.setAdapter(mAdapter);


    }

    public void chooseEnglish(View view) {

        //display chosen english in edit text box <- only if a phrase/ word is chosen
//        if (chosenPhrase!=null){
//
//        }

    }

    public void updateAndSaveEnglish(View view) {

        // X - remove old english phrase from db
        // X - insert new phrase into db
        // update english in db

        // refresh page with new info ------------
    }
}
