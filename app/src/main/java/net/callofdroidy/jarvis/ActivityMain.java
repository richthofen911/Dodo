package net.callofdroidy.jarvis;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class ActivityMain extends AppCompatActivity{
    private static final int SPEECH_REQUEST_CODE = 0;
    private TextView tv_spokenText;
    private static SpeechRecognizer mySpeechRecognizer = null;

    private Intent intentSpeechRecognizer;
    private RecognitionListener myRecognitionListener;
    private ToolBox myToolBox;
    private VoiceCommandExecutor myVoiceCommandExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_spokenText = (TextView) findViewById(R.id.tv_spokenText);
        myToolBox = ToolBox.getInstance(getApplicationContext());
        myVoiceCommandExecutor = VoiceCommandExecutor.getInstance(getApplicationContext(), getApplication());

        intentSpeechRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentSpeechRecognizer.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        intentSpeechRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSpeechRecognizer.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplication().getPackageName());

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
                myVoiceCommandExecutor.speak("At your service.");
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
                    myVoiceCommandExecutor.exec(results.get(0));
            }
            @Override
            public void onError(int error) {
                Log.e("listener step", "onError, " + RecognizerErrorTranslator.translateErrorCode(error));
                myVoiceCommandExecutor.exec("!@#$%" + RecognizerErrorTranslator.translateErrorCode(error));
            }
        };

        findViewById(R.id.btn_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("speak content", tv_spokenText.getText().toString());
                myVoiceCommandExecutor.speak(tv_spokenText.getText().toString());
            }
        });

        findViewById(R.id.btn_recognition).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechRecognizerService();
            }
        });

        findViewById(R.id.btn_wakeOnLan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myToolBox.wakeOnLan("192.168.128.255", "54:a0:50:52:16:76"); //my fedora in the office
            }
        });
    }

    // Create an intent that can start the Speech Recognizer activity
    private void startSpeechRecognizerActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    private void startSpeechRecognizerService(){
        if(mySpeechRecognizer == null){
            Log.e("recognizer", "is null, creating one");
            mySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            mySpeechRecognizer.setRecognitionListener(myRecognitionListener);
        }
        if(!SpeechRecognizer.isRecognitionAvailable(this))
            Log.e("recognizer", "is not available");
        else{
            mySpeechRecognizer.startListening(intentSpeechRecognizer);
            Log.e("recognizer", "start listening");
        }
    }

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

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(mySpeechRecognizer != null)
            mySpeechRecognizer.destroy();
        myVoiceCommandExecutor.destroy();
    }
}
