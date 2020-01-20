package com.v0k0.reddittopposts.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.v0k0.reddittopposts.Network.JSONParser;
import com.v0k0.reddittopposts.Network.RedditConnector;
import com.v0k0.reddittopposts.R;
import com.v0k0.reddittopposts.pojo.PostItem;

import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String LIST_KEY = "list_key";
    private ArrayList<PostItem> redditPosts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if (checkConnectionState()){
            Intent intent = new Intent(SplashScreenActivity.this, FeedActivity.class);
            redditPosts = JSONParser.getPostsFromJSON(RedditConnector.getJSONFromReddit());
            intent.putParcelableArrayListExtra(LIST_KEY, redditPosts);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.internet_connection_state), Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkConnectionState(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo currentState = connectivityManager.getActiveNetworkInfo();
            return currentState != null && currentState.isConnected();
        }

        return false;
    }

}
