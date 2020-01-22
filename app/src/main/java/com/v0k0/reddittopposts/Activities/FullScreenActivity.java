package com.v0k0.reddittopposts.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.v0k0.reddittopposts.Utils.UserPermissions;
import com.v0k0.reddittopposts.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class FullScreenActivity extends AppCompatActivity {

    private ImageView imageViewBackToFeed;
    private ImageView imageViewDownload;
    private ImageView imageViewOpened;
    private boolean isButtonsHidden;
    private boolean wasDownloaded;
    private static final String KEY_BIG_IMAGE = "big_image";
    private static final String DOWNLOAD_STATUS = "download";
    Animation animClick;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        if (savedInstanceState == null){
            wasDownloaded = false;
        } else {
            wasDownloaded = savedInstanceState.getBoolean(DOWNLOAD_STATUS);
        }


        imageViewBackToFeed = findViewById(R.id.imageViewBackToFeed);
        imageViewDownload = findViewById(R.id.imageViewSave);
        imageViewOpened = findViewById(R.id.imageViewBigPost);
        isButtonsHidden = false;
        imageViewOpened.setOnClickListener(onOpenedImageClick);
        imageViewDownload.setOnClickListener(onDownloadImageClick);
        imageViewBackToFeed.setOnClickListener(onBackToFeedClick);

        Intent intent = getIntent();
        String bigPicturePath = intent.getStringExtra(KEY_BIG_IMAGE);
        Picasso.get().load(bigPicturePath).into(imageViewOpened);

        animClick = AnimationUtils.loadAnimation(this, R.anim.fading_out_animation);

    }

    private ImageView.OnClickListener onOpenedImageClick = (view) -> {
        showHideUIElements(isButtonsHidden, view.getContext());
        isButtonsHidden = !isButtonsHidden;
    };

    private ImageView.OnClickListener onDownloadImageClick = (view) -> {
        imageViewDownload.startAnimation(animClick);
        if (!wasDownloaded) {
            downloadImageToGallery();
        }
    };

    private ImageView.OnClickListener onBackToFeedClick = (view) -> {
        imageViewBackToFeed.startAnimation(animClick);
        finish();
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

    private void downloadImageToGallery() {

        UserPermissions.checkStoragePermission(this);

        BitmapDrawable drawable = (BitmapDrawable) imageViewOpened.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        File sdCardDirectory = Environment.getExternalStorageDirectory();
        File redditDirectory = new File(sdCardDirectory.getAbsolutePath() + "/reddit");
        redditDirectory.mkdirs();
        String fileName = String.format(Locale.getDefault(), "%d.jpg", System.currentTimeMillis());
        File outFile = new File(redditDirectory, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wasDownloaded = true;
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(outFile));
        sendBroadcast(intent);

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(DOWNLOAD_STATUS, wasDownloaded);
    }
}
