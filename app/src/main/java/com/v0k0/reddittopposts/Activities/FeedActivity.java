package com.v0k0.reddittopposts.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.v0k0.reddittopposts.R;

public class FeedActivity extends AppCompatActivity {

    private ImageView imageViewGo;
    private ImageView imageViewBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewGo = findViewById(R.id.btn_get_forward);
        imageViewBack = findViewById(R.id.btn_get_back);
    }

}
