package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Opening of Activities using button clicks

//        add phrases button
        Button addPhrasesButton = findViewById(R.id.add_phrases_button);
        addPhrasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPhrases.class);
                startActivity(intent);
            }
        });

//        display phrases button
        Button displayPhrasesButton = findViewById(R.id.display_phrases_button);
        displayPhrasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayPhrases.class);
                startActivity(intent);
            }
        });

//        edit phrases button
        Button editPhrasesButton = findViewById(R.id.edit_phrases_button);
        editPhrasesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditPhrases.class);
                startActivity(intent);
            }
        });

//        language subscription button
        Button langSubscriptionButton = findViewById(R.id.language_subscription_button);
        langSubscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LanguageSubscription.class);
                startActivity(intent);
            }
        });

//        translate button
        Button translateButton = findViewById(R.id.translate_button);
        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TranslateActivity.class);
                startActivity(intent);
            }
        });

    }
}
