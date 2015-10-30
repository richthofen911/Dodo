package net.callofdroidy.jarvis;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.actions.SearchIntents;

public class ActivitySearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intentGoogleNow = getIntent();

        if(intentGoogleNow.getAction().equals(SearchIntents.ACTION_SEARCH)){
            String query = intentGoogleNow.getStringExtra(SearchManager.QUERY);
            Log.e("search query", query);
            ((TextView) findViewById(R.id.tv_spokenSearch)).setText(query);
            //the working patter would be "Ok Google, search [query string can be picked up] on [my app name]"
        }
    }
}
