package net.callofdroidy.jarvis;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceiverSearchIntent extends BroadcastReceiver {
    public ReceiverSearchIntent() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("receive intent", intent.getExtras().getString("name"));
        //xif(intent.getExtras().getString("name").equals(""))
    }
}
