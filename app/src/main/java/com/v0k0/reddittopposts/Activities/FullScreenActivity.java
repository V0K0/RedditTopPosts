package com.v0k0.reddittopposts.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.v0k0.reddittopposts.R;

public class FullScreenActivity extends AppCompatActivity {

    private ImageView imageViewBackToFeed;
    private ImageView imageViewDownload;
    private ImageView imageViewOpened;
    private boolean isButtonsHidden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        imageViewBackToFeed = findViewById(R.id.imageViewBackToFeed);
        imageViewDownload = findViewById(R.id.imageViewSave);
        imageViewOpened = findViewById(R.id.imageViewOpened);

        isButtonsHidden = true;

        imageViewOpened.setOnClickListener(onOpenedImageClick);
    }

    private ImageView.OnClickListener onOpenedImageClick = (view) -> {
        showHideUIElements(isButtonsHidden, view.getContext());
        isButtonsHidden = !isButtonsHidden;
    };

    private void showHideUIElements(boolean status, Context context) {
        if (status) {
            Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fading_in_animation);
            fadeIn.setAnimationListener(fadeInAnimationListener);
            imageViewDownload.startAnimation(fadeIn);
            imageViewBackToFeed.startAnimation(fadeIn);
        } else {
            Animation fadeOut = AnimationUtils.loadAnimation(context, R.anim.fading_out_animation);
            fadeOut.setAnimationListener(fadeOutListener);
            imageViewDownload.startAnimation(fadeOut);
            imageViewBackToFeed.startAnimation(fadeOut);
        }
    }

    private Animation.AnimationListener fadeInAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            imageViewDownload.setVisibility(View.VISIBLE);
            imageViewBackToFeed.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener fadeOutListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            imageViewDownload.setVisibility(View.INVISIBLE);
            imageViewBackToFeed.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    };

}
