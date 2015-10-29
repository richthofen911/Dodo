package net.callofdroidy.jarvis;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by admin on 29/10/15.
 */
public class VoiceCommandExecutor {
    private static VoiceCommandExecutor instance;
    private Context cxt;
    private TextToSpeech textToSpeech;

    private VoiceCommandExecutor(Context context){
        cxt = context;
        textToSpeech = new TextToSpeech(cxt, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.CANADA);
                    Log.e("text to speech", "is ready");
                }else
                    Log.e("text to speech", "init failed. Error code: " + status);
            }
        });
    }

    public static synchronized VoiceCommandExecutor getInstance(Context context){
        if(instance == null)
            instance = new VoiceCommandExecutor(context);
        return instance;
    }

    public String exec(String cmd){
        if(cmd.startsWith("!@#$%")){ //onError result
            speak("command error, " + cmd.substring(5, cmd.length()));
        }else {
            speak("executing command " + cmd);
            //do more concrete actions
        }
        return null;
    }

    /**
     * this method handles commands start with "web" and do google search
     * @param query
     */
    public void webSearching(String query) { //query is the desired text you want to input, such as "toronto weather"
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, query);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        cxt.startActivity(intent);
    }

    /**
     * this method handles commands start with "application", it starts applications on my devices
     * the pattern should be "application + [application name] + [action](start/stop)"
     * and do some HTTP request to let the device end start the application
     * A device will have a alias mapping to an IP address
     * @param query
     */
    public void applicationCalling(String query){

    }

    public void speak(String speakContent){
        textToSpeech.speak(speakContent, TextToSpeech.QUEUE_FLUSH, null, "speak utility");
    }

    public void destroy(){
        textToSpeech.shutdown();
    }
}
