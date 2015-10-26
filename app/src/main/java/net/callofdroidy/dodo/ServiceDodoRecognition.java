package net.callofdroidy.dodo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.RecognitionService;
import android.util.Log;

public class ServiceDodoRecognition extends RecognitionService {
    public ServiceDodoRecognition(){}

    @Override
    public void onStartListening(Intent recognizerIntent, RecognitionService.Callback listener){
        Log.e("on start listening", "");
    }

    @Override
    public void onStopListening(RecognitionService.Callback listener){
        Log.e("on stop listening", "");
    }

    @Override
    public void onCancel(RecognitionService.Callback listener){
        //do something to shut down recognition here.
        Log.e("on cancel", "");
    }
}
