package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dictionary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        // open activity Dictionary Language List
        Button showLangListButton = findViewById(R.id.subscribe_button);
        showLangListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Dictionary.this, DictionarySubscriptions.class);
//                startActivity(intent);
            }
        });
    }
}
