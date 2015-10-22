package net.callofdroidy.googlegreetings;

import android.app.SearchManager;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.callofdroidy.launchgooglenow.R;

import java.util.Locale;

public class ActivityMain extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textToSpeech = new TextToSpeech(ActivityMain.this, this);

        findViewById(R.id.btn_speak).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speakContent = "hello, I am Google Text To Speech";
                Log.e("speak content", speakContent);
                textToSpeech.speak(speakContent, TextToSpeech.QUEUE_FLUSH, null, "test");
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
