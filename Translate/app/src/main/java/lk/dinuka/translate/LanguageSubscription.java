package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class LanguageSubscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_subscription);

        // get and display all foreign languages stored in a separate database (foreignLanguagesDB?)
        // with boolean value of subscribed
    }


    public void updateLanguages(View view) {
        // save boolean statuses of all changed languages
        // take new changes into a HashMap and change only those in the db?

    }
}
