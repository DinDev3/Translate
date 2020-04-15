package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;

import lk.dinuka.translate.services.MyDisplayAdapter;

public class DisplayPhrases extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyDisplayAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);

        recyclerView = findViewById(R.id.display_recycler_view);
        recyclerView.setHasFixedSize(true);     // change in content won't change the layout size of the RecyclerView

        // Use a linear layout within the recycler view
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify the adapter (a bridge between a UI component and a data source)
        mAdapter = new MyDisplayAdapter(MainActivity.allEnglishFromDB);          // insert initial list of words here
        recyclerView.setAdapter(mAdapter);

        // -----------------------

        // search box functionality
        EditText searchText = findViewById(R.id.search_box);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {           // Editable editable is basically the content of the EditText
                filter(editable.toString());
            }
        });

    }

    private void filter(String filterText) {            // every time a letter is input into the search-box, this method is called
        ArrayList<String> filteredPhraseList = new ArrayList<>();
        for (String enteredSearch : MainActivity.allEnglishFromDB) {
            if (enteredSearch.toLowerCase().contains(filterText.toLowerCase())) {     // if entered text is contained in the phrases
                filteredPhraseList.add(enteredSearch);
            }
        }
        mAdapter.filteredResults(filteredPhraseList);        // sending filtered list into adapter
    }
}

/*
References:
Recycler-view
https://stackoverflow.com/questions/25477860/error-inflating-class-android-support-v7-widget-recyclerview

https://developer.android.com/guide/topics/ui/layout/recyclerview
https://www.javatpoint.com/android-recyclerview-list-example

https://stackoverflow.com/questions/47455203/recyclerview-in-constraintlayout-overlapping-other-elements
 */