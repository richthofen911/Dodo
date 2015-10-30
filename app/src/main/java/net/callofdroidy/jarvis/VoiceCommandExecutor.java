package net.callofdroidy.jarvis;

import android.app.Application;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.android.volley.Request;

import java.util.Locale;

/**
 * Created by admin on 29/10/15.
 */
public class VoiceCommandExecutor {
    private static VoiceCommandExecutor instance;
    private Context cxt;
    private Application app;
    private TextToSpeech textToSpeech;
    private APICaller myAPICaller;
    private MySingletonRequestQueue mySingletonRequestQueue;
    private SharedPreferences spMapDeviceIP;

    private VoiceCommandExecutor(Context context, Application application){
        cxt = context;
        mySingletonRequestQueue = MySingletonRequestQueue.getInstance(context);
        myAPICaller = APICaller.getInstance(context, mySingletonRequestQueue);
        spMapDeviceIP = application.getSharedPreferences("MapDeviceIP", 0);
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

    public static synchronized VoiceCommandExecutor getInstance(Context context, Application application){
        if(instance == null)
            instance = new VoiceCommandExecutor(context, application);
        return instance;
    }

    public String exec(String cmd){
        if(cmd.startsWith("!@#$%")){ //onError result
            speak("command error, " + cmd.substring(5, cmd.length()));
        }else {
            speak("executing command " + cmd);
            //do more concrete actions
            String[] cmdInArray = cmd.split(" ");
            switch (cmdInArray[0]){
                case "application":
                    controlApp(cmdInArray[1], cmdInArray[2], cmdInArray[3]);
                    break;
                case "wake":
                    break;
            }
        }
        return null;
    }

    /**
     * this method handles commands start with "web" and do google search
     * @param keyword
     */
    public void searchWeb(String keyword) { //query is the desired text you want to input, such as "toronto weather"
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, keyword);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        cxt.startActivity(intent);
    }

    /**
     * this method handles commands start with "application", it starts applications on my devices
     * the pattern should be "application + [device name] + [application name] + [action](start/stop)"
     * and do some HTTP request to let the device end start the application
     * A device will have a alias mapping to an IP address
     * @param deviceName
     * @param appName
     * @param action
     */
    public void controlApp(String deviceName, String appName, String action){
        myAPICaller
                .setAPI("http://" + spMapDeviceIP.getString(deviceName, ""), "/applauncher",  "appname=" + appName + "&action=" + action, null, Request.Method.GET)
                .exec(new APICaller.VolleyCallback() {
                    @Override
                    public void onDelivered(String result) {
                        speak(result);
                    }
                });
    }

    public void speak(String speakContent){
        textToSpeech.speak(speakContent, TextToSpeech.QUEUE_FLUSH, null, "speak utility");
    }

    public void destroy(){
        textToSpeech.shutdown();
    }
}
