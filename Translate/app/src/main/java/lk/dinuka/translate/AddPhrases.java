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
        EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());
        String english = enterEnglish.getText().toString();
        englishRepository.insertTask(english);

        displayToast("The newly entered text was saved.");
    }

    public void clearText(View view) {
        enterEnglish.setText("");
    }


    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }
}
