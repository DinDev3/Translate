package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class EditPhrases extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phrases);

        // get all English phrases from db and display
    }

    public void chooseEnglish(View view) {

        //display chosen english in edit text box <- only if a phrase/ word is chosen
//        if (chosenPhrase!=null){
//
//        }

    }

    public void updateAndSaveEnglish(View view) {

        // remove old english phrase from db


        // insert new phrase into db


        // refresh page with new info ------------
    }
}
