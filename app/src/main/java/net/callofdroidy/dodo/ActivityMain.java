package net.callofdroidy.dodo;

import android.app.SearchManager;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import net.callofdroidy.launchgooglenow.R;

import java.util.List;
import java.util.Locale;

public class ActivityMain extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private TextToSpeech textToSpeech;
    private static final int SPEECH_REQUEST_CODE = 0;
    private TextView tv_spokenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(ActivityMain.this, this);
        tv_spokenText = (TextView) findViewById(R.id.tv_spokenText);

        findViewById(R.id.btn_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakContent = tv_spokenText.getText().toString();
                Log.e("speak content", speakContent);
                textToSpeech.speak(speakContent, TextToSpeech.QUEUE_FLUSH, null, "test");
            }
        });

        findViewById(R.id.btn_recognition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displaySpeechRecognizer();
            }
        });

        //searchWeb("toronto weather");

    }

    public void searchWeb(String query) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(intent);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    /**
     * This callback is invoked when the Speech Recognizer returns.
     * This is where you process the intent and extract the speech text from the intent.
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            tv_spokenText.setText(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    //implements TextToSpeech.OnInitListener
    @Override
    public void onInit(int status){
        if(status != TextToSpeech.ERROR){
            textToSpeech.setLanguage(Locale.CANADA);
            Log.e("text to speech", "ready");
        }else {
            Log.e("text to speech", "init failed. Error code: " + status);
        }
    }
}
