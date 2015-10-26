package net.callofdroidy.dodo;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import java.util.Locale;

public class ActivityMain extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private TextToSpeech textToSpeech;
    private static final int SPEECH_REQUEST_CODE = 0;
    private TextView tv_spokenText;
    private String spokenText = "no voice input";
    private boolean stopRecogonition = false;
    //private SpeechRecognizer mySpeechRecognizer;

    //private SpeechRecognizer mSpeechRecognizer;

    private Intent intentSpeechRecognizer;
    private RecognitionListener myRecognitionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(ActivityMain.this, this);
        tv_spokenText = (TextView) findViewById(R.id.tv_spokenText);

        intentSpeechRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentSpeechRecognizer.putExtra("android.speech.extra.DICTATION_MODE", true);
        intentSpeechRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        myRecognitionListener = new RecognitionListener(){
            @Override
            public void onBeginningOfSpeech(){ //this happens when it recognizes a start of voice input
                Log.e("listener step", "onBeginningOfSpeech");
            }
            @Override
            public void onEndOfSpeech(){ //this happens when it recognizes an end of voice input
                Log.e("listener step", "onEndOfSpeech");
            }
            @Override
            public void onBufferReceived(byte[] buffer){
                Log.e("listener step", "onBufferReceived");
            }
            @Override
            public void onReadyForSpeech(Bundle params){
                Log.e("listener step", "onReadyForSpeech");
                textToSpeech.speak("At your service, My Lord", TextToSpeech.QUEUE_FLUSH, null, "test");
            }
            @Override
            public void onEvent(int eventType, Bundle params){
                Log.e("listener step", "onEvent");
            }

            @Override
            public void onRmsChanged(float rmsdB){
                //Log.e("listener step", "onRmsChanged");
            }

            @Override
            public void onPartialResults(Bundle partialResults){
                Log.e("listener step", "onPartialResults");
            }
            @Override
            public void onResults(Bundle b) {
                Log.e("listener step", "onResults");
                List<String> results = b.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if(results.size() > 0)
                    spokenText = results.get(0);
                // Do something with spokenText
                tv_spokenText.setText(spokenText);
                startSpeechRecognizerService();
                //textToSpeech.speak(tv_spokenText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "test");
/*
                if(!stopRecogonition){
                    restartSpeechRecognizerService();
                }
*/
            }
            @Override
            public void onError(int error) {
                Log.e("listener step", "onError, error code: " + error);
                tv_spokenText.setText(spokenText);
                startSpeechRecognizerService();
                //textToSpeech.speak(tv_spokenText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "test");
/*
                if(!stopRecogonition){
                    restartSpeechRecognizerService();
                }
*/
            }
        };

        findViewById(R.id.btn_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("speak content", tv_spokenText.getText().toString());
                textToSpeech.speak(tv_spokenText.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, "test");
            }
        });

        findViewById(R.id.btn_recognition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechRecognizerService();
            }
        });
    }

    public void searchWeb(String query) { //query is the desired text you want to input, such as "toronto weather"
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(intent);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void startSpeechRecognizerActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    private void startSpeechRecognizerService(){
        //mySpeechRecognizer = null;
        SpeechRecognizer mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        if(!SpeechRecognizer.isRecognitionAvailable(this))
            Log.e("recognizer", "is not available");
        mySpeechRecognizer.setRecognitionListener(myRecognitionListener);
        mySpeechRecognizer.startListening(intentSpeechRecognizer);
    }

    /*
    private void restartSpeechRecognizerService() {
        Log.e("restarting", "speech recognition");
        mySpeechRecognizer.stopListening();
        mySpeechRecognizer.cancel();
        mySpeechRecognizer.startListening(intentSpeechRecognizer);
    }
    */

    /**
     * This callback is invoked when the Speech Recognizer returns.
     * This is where you process the intent and extract the speech text from the intent.
     */
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            tv_spokenText.setText(spokenText);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/
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

    @Override
    public void onDestroy(){
        super.onDestroy();

        textToSpeech.shutdown();

    }
}
