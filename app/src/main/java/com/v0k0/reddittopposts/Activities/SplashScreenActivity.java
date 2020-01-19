package com.v0k0.reddittopposts.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.v0k0.reddittopposts.Network.JSONParser;
import com.v0k0.reddittopposts.Network.RedditConnector;
import com.v0k0.reddittopposts.R;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String LIST_KEY = "list_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (checkConnectionState()){
            Intent intent = new Intent(SplashScreenActivity.this, FeedActivity.class);
            intent.putParcelableArrayListExtra(LIST_KEY, JSONParser.getPostsFromJSON(RedditConnector.getJSONFromReddit()));
            startActivity(intent);
        } else {
            Toast.makeText(this, "Check internet connection", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkConnectionState(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
