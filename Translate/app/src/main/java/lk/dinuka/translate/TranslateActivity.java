package lk.dinuka.translate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ibm.cloud.sdk.core.http.HttpMediaType;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.developer_cloud.android.library.audio.StreamPlayer;
import com.ibm.watson.language_translator.v3.LanguageTranslator;
import com.ibm.watson.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.language_translator.v3.model.TranslationResult;
import com.ibm.watson.language_translator.v3.util.Language;
import com.ibm.watson.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.text_to_speech.v1.model.SynthesizeOptions;

public class TranslateActivity extends AppCompatActivity {
    private LanguageTranslator translationService;          // translation service
    private TextToSpeech textService;           // Text to speech service


    private String translationLanguage;
    private String translationText;
    private TextView displayTranslation;
    public String selectedSpinnerLanguageLabel;         // holds the translation language/ language reference code???

    private StreamPlayer player = new StreamPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        displayTranslation = findViewById(R.id.translated_textView);        // TextView to display translation
    }


    public void translateChosenEnglish(View view) {

        // get translation language from spinner and assign here
//        translationLanguage = findViewById(R.id.);



        // get English word/ phrase to be translated
//        translationText = findViewById(R.id.);


        // translate using Watson Translator
        translationService = initLanguageTranslatorService();           // connect & initiate to the cloud translation service
        new TranslationTask().execute(translationText);

    }

    public void pronounceTranslation(View view) {  // pronounce translated word/ phrase

        textService = initTextToSpeechService();           // connect & initiate to the cloud text to speech service


        // pass in translation language here ---------------->>>>>>>


        // speak displayed translation
//        new SynthesisTask().execute(displayTranslation.getText().toString(),EN_US_LISAVOICE);     ----->>>>>????


    }


    // ---------------------

    private LanguageTranslator initLanguageTranslatorService() {           // connect & initiate to the cloud translation service
        Authenticator authenticator = new IamAuthenticator("2daMreRDE8V5zPRO3enCVHGUCH1sQJs-Kdq8ryPn4-ij");

        LanguageTranslator service = new LanguageTranslator("2018-05-01", authenticator);

        service.setServiceUrl("https://api.us-south.language-translator.watson.cloud.ibm.com/instances/caf1b5bc-ff11-4271-96cf-93372088290d");

        return service;
    }


    private class TranslationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            TranslateOptions translateOptions = new TranslateOptions.Builder()
                    .addText(params[0])
                    .source(Language.ENGLISH)
                    .target("es")                       // this translates only into French ---- pass in translationLanguage here, to get required language
                    .build();

            TranslationResult result = translationService.translate(translateOptions).execute().getResult();

            String firstTranslation = result.getTranslations().get(0).getTranslation();

            return firstTranslation;
        }

        @Override
        protected void onPostExecute(String translatedText) {
            super.onPostExecute(translatedText);
            displayTranslation.setText(translatedText);
        }
    }


    // ---------------------

    private TextToSpeech initTextToSpeechService() {
        Authenticator authenticator = new IamAuthenticator("fV_5OUIol2WXx_pXQ2oG9-PWJFsbBr2I6tiDKOrWUB7k");
        TextToSpeech service = new TextToSpeech(authenticator);
        service.setServiceUrl("https://api.us-south.text-to-speech.watson.cloud.ibm.com/instances/2aa0807f-fff7-4ea6-a9aa-5828fa2f2020");
        return service;
    }


    private class SynthesisTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            SynthesizeOptions synthesizeOptions = new SynthesizeOptions.Builder()
                    .text(params[0])            // only one String parameter is entered. This is the translated text.
                    .voice(SynthesizeOptions.Voice.EN_US_LISAVOICE)     // ------>>>>>>this will speak out only in English. params[1] can be used to get translated language
                    .accept(HttpMediaType.AUDIO_WAV)
                    .build();
            player.playStream(textService.synthesize(synthesizeOptions).execute().getResult());

            return "Did synthesize";
        }
    }

}
