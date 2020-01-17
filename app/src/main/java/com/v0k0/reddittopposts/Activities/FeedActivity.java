package com.v0k0.reddittopposts.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.v0k0.reddittopposts.Network.RedditConnector;
import com.v0k0.reddittopposts.R;


import org.json.JSONObject;

public class FeedActivity extends AppCompatActivity {

    private ImageView imageViewGo;
    private ImageView imageViewBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        imageViewGo = findViewById(R.id.btn_get_forward);
        imageViewBack = findViewById(R.id.btn_get_back);

       JSONObject jsonObject = RedditConnector.getJSONFromReddit("");
       if (jsonObject != null) {
           Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
       }


    }

}
