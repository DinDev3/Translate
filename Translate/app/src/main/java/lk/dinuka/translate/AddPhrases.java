package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import lk.dinuka.translate.databases.english.EnglishRepository;

public class AddPhrases extends AppCompatActivity {
    private EditText enterEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);

        enterEnglish = findViewById(R.id.enter_to_save_multiText);
    }

    public void saveEnteredEnglish(View view) {
        // add this to the db
        String english = enterEnglish.getText().toString();

        if (english.length() > 0) {             // there should be at least one letter to add into the db
            EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());
            englishRepository.insertTask(english);          // insert new phrase into db
            MainActivity.allEnglishFromDB.add(english);     // update arrayList of of all phrases
            displayToast("The newly entered text was saved.");

        } else {
            displayToast("The phrase must have at least one character");
        }
    }

    public void clearText(View view) {
        enterEnglish.setText("");
    }


    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
