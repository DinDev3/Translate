package lk.dinuka.translate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;

import java.util.List;

import lk.dinuka.translate.databases.EnglishEntered;
import lk.dinuka.translate.databases.EnglishRepository;

public class DisplayPhrases extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_phrases);

        // get all english phrases from db and display
        EnglishRepository englishRepository = new EnglishRepository(getApplicationContext());

        englishRepository.getEnglishFromDB().observe(this, new Observer<List<EnglishEntered>>() {
            @Override
            public void onChanged(@Nullable List<EnglishEntered> allEnglish) {
                for(EnglishEntered english : allEnglish) {
                    System.out.println("-----------------------");
                    System.out.println(english.getId());
                    System.out.println(english.getEnglish());
                    System.out.println(english.getCreatedAt());
                    System.out.println(english.getUpdatedAt());
                }
            }
        });
    }
}
