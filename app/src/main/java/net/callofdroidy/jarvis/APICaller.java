package net.callofdroidy.jarvis;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by admin on 30/10/15.
 */
public class APICaller {
    private String APIUrlStr;
    private String APIUrlEncoded;

    private int requestMethod; //Request.Method.GET is an int
    private Map<String, String> postParams; //this is for POST request
    private StringRequest requestCallAPI;
    private MySingletonRequestQueue requestQueue;
    private Context context;
    private static APICaller singletonInstance;

    public APICaller(Context cxt, MySingletonRequestQueue myRequestQueue){
        this.context = cxt;
        requestQueue = myRequestQueue;
    }

    public static synchronized APICaller getInstance(Context cxt, MySingletonRequestQueue myRequestQueue){
        if(singletonInstance == null){
            singletonInstance = new APICaller(cxt, myRequestQueue);
        }
        return singletonInstance;
    }

    public APICaller setAPI(String urlBase, String urlPath, String urlParams, Map<String, String> postParams, int method){
        if(urlParams == null)
            APIUrlStr = urlBase + urlPath;
        else
            APIUrlStr = urlBase + urlPath + urlParams;
        APIUrlEncoded = Uri.encode(APIUrlStr).replace("%3A", ":");
        APIUrlEncoded = APIUrlEncoded.replace("%2F", "/");
        APIUrlEncoded = APIUrlEncoded.replace("%3F", "?");
        APIUrlEncoded = APIUrlEncoded.replace("%3D", "=");
        APIUrlEncoded = APIUrlEncoded.replace("%26", "&");
        Log.e("url encoded", APIUrlEncoded);
        if(postParams != null && method == Request.Method.POST)
            this.postParams = postParams;
        else {
            Log.e("postParams", "is illegal here");
        }
        requestMethod = method;

        return this;
    }

    public interface VolleyCallback{
        void onDelivered(String result);
    }

    public void exec(final VolleyCallback callback){
        if(APIUrlEncoded == null){
            callback.onDelivered("API has not been set yet");
        }else {
            final Timer timerRequestExec = new Timer();
            requestCallAPI = new StringRequest(requestMethod, APIUrlEncoded, new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    requestCallAPI.markDelivered();
                    timerRequestExec.cancel();
                    callback.onDelivered(response.replace("\\", ""));
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    requestCallAPI.markDelivered();
                    timerRequestExec.cancel();
                    callback.onDelivered(error.toString() + "from error");
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return postParams;
                }
            };
            requestCallAPI.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(requestCallAPI);
            timerRequestExec.schedule(new TimerTask() { //if the request doesn't get any response in 5 seconds, cancel it and pop up network issus
                @Override
                public void run() {
                    if(!requestCallAPI.hasHadResponseDelivered()){
                        requestCallAPI.cancel();
                        Toast.makeText(context, "Bad Network Status", Toast.LENGTH_SHORT).show();
                    }
                }
            }, 5000);
        }
    }
}
