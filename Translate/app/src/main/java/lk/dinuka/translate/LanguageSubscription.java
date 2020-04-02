package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import lk.dinuka.translate.util.MyLanguageAdapter;

public class LanguageSubscription extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);

        // get and display all foreign languages stored in a separate database (foreignLanguagesDB?)
        // with boolean value of subscribed


        recyclerView = findViewById(R.id.language_sub_recycler_view);


        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // specify the adapter (a bridge between a UI component and a data source)
//        mAdapter = new MyLanguageAdapter(MainActivity.allForeignLanguages);          // insert list of words here
//        recyclerView.setAdapter(mAdapter);

    }


    public void updateLanguages(View view) {
        // save boolean statuses of all changed languages
        // take new changes into a HashMap and change only those in the db?

    }
}
