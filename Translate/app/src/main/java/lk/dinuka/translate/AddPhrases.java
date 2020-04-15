package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Collections;

import lk.dinuka.translate.databases.repositories.EnglishRepository;

import static lk.dinuka.translate.MainActivity.allEnglishFromDB;

public class AddPhrases extends AppCompatActivity {
    private EditText mEnterEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);

        mEnterEnglish = findViewById(R.id.enter_to_save_multiText);
    }

    public void saveEnteredEnglish(View view) {
        // add this to the db
        String english = mEnterEnglish.getText().toString();

        if (english.length() > 0) {             // there should be at least one letter to add into the db
            EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());
            englishRepository.insertTask(english);          // insert new phrase into db
            MainActivity.allEnglishFromDB.add(english);     // update arrayList of of all phrases
            displayToast("The newly entered text was saved.");

            Collections.sort(allEnglishFromDB);         // sort existing english words in alphabetical order

        } else {
            displayToast("The phrase must have at least one character");
        }
    }

    public void clearText(View view) {
        mEnterEnglish.setText("");
    }


    public void displayToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT);
        View view = toast.getView();

        //Gets the actual oval background of the Toast then sets the colour filter
        view.getBackground().setColorFilter(Color.parseColor("#56ccf2"), PorterDuff.Mode.SRC_IN);

        toast.show();
    }
}

/*
References:
change toast color
https://stackoverflow.com/a/49010394/11005638

 */