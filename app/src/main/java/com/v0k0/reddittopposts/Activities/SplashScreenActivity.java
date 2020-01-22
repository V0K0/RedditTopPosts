package com.v0k0.reddittopposts.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.v0k0.reddittopposts.Network.JSONParser;
import com.v0k0.reddittopposts.Network.RedditConnector;
import com.v0k0.reddittopposts.R;
import com.v0k0.reddittopposts.pojo.PostItem;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class SplashScreenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final String LIST_KEY = "list_key";
    private static final String KEY_URL = "url";
    private ArrayList<PostItem> redditPosts = new ArrayList<>();
    private static final int LOADER_ID = 1;
    private LoaderManager loaderManager;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = findViewById(R.id.progressBarSplashLoading);
        progressBar.setVisibility(View.VISIBLE);
        if (checkConnectionState()){
            loaderManager = LoaderManager.getInstance(this);
            URL url = RedditConnector.buildUrl();
            Bundle bundle = new Bundle();
            bundle.putString(KEY_URL, url.toString());
            loaderManager.restartLoader(LOADER_ID, bundle, this);
        } else {
            Toast.makeText(this, getString(R.string.internet_connection_state), Toast.LENGTH_LONG).show();
        }

    }

    private boolean checkConnectionState(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo currentState = connectivityManager.getActiveNetworkInfo();
            return currentState != null;
        }

        return false;
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        return new RedditConnector.JSONLoader(this, args);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        progressBar.setVisibility(View.INVISIBLE);
        redditPosts = JSONParser.getPostsFromJSON(data);
        loaderManager.destroyLoader(LOADER_ID);
        Intent intent = new Intent(SplashScreenActivity.this, FeedActivity.class);
        intent.putParcelableArrayListExtra(LIST_KEY, redditPosts);
        startActivity(intent);
        finish();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}
