package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import lk.dinuka.translate.databases.english.EnglishRepository;

public class AddPhrases extends AppCompatActivity {
    private EditText enterEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phrases);
    }

    public void saveEnteredEnglish(View view) {
        enterEnglish = findViewById(R.id.enter_to_save_multiText);

        // add this to the db
        EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());
        String english = enterEnglish.getText().toString();
        englishRepository.insertTask(english);
    }
}
