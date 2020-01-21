package com.v0k0.reddittopposts.Network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.concurrent.ExecutionException;

public class RedditConnector {

    private static final String BASE_URL = "https://www.reddit.com/top.json";
    private static final String PARAM_LIMIT = "limit";
    private static final String PARAM_AFTER = "after";

    private static final int POSTS_COUNT = 10;
    private static final String PARAMS_BEFORE = "before";


    public static URL buildUrl(boolean isAfter, String postId) {
        Uri uri;
        URL resultURL = null;
        if (postId.isEmpty()) {
            return null;
        }
        if (isAfter) {
            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_LIMIT, Integer.toString(POSTS_COUNT))
                    .appendQueryParameter(PARAM_AFTER, postId)
                    .build();
        } else {
            uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(PARAM_LIMIT, Integer.toString(POSTS_COUNT))
                    .appendQueryParameter(PARAMS_BEFORE, postId)
                    .build();
        }

        try {
            resultURL = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return resultURL;
    }

    public static URL buildUrl() {
        Uri uri;
        URL resultURL = null;
        uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_LIMIT, Integer.toString(POSTS_COUNT))
                .build();
        try {
            resultURL = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return resultURL;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject> {

        private Bundle bundle;
        private static final String KEY_URL = "url";

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            if (bundle == null) {
                return null;
            }
            String urlAsString = bundle.getString(KEY_URL);
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url == null){
                return null;
            }

            JSONObject jsonObject = null;
            HttpURLConnection httpURLConnection = null;
            try {
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder builder = new StringBuilder();
                String jsonLine = reader.readLine();
                while (jsonLine != null) {
                    builder.append(jsonLine);
                    jsonLine = reader.readLine();
                }

                jsonObject = new JSONObject(builder.toString());

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return jsonObject;

        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }
    }

}
