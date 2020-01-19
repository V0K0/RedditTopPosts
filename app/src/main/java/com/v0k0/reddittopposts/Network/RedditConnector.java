package com.v0k0.reddittopposts.Network;

import android.net.Uri;
import android.os.AsyncTask;

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


    private static URL buildUrl(String afterPost) {
        Uri uri;
        URL resultURL = null;
        if (afterPost.isEmpty()) {
            return null;
        }

        uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_LIMIT, Integer.toString(POSTS_COUNT))
                .appendQueryParameter(PARAM_AFTER, afterPost)
                .build();

        try {
            resultURL = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return resultURL;
    }

    private static URL buildUrl() {
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


    public static JSONObject getJSONFromReddit(String afterPost) {
        JSONObject jsonResult = null;
        URL url = buildUrl(afterPost);
        try {
            jsonResult = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    public static JSONObject getJSONFromReddit() {
        JSONObject jsonResult = null;
        URL url = buildUrl();
        try {
            jsonResult = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }


    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject jsonObject = null;
            HttpURLConnection httpURLConnection = null;
            if (urls == null || urls.length == 0) {
                return null;
            }

            try {
                httpURLConnection = (HttpURLConnection) urls[0].openConnection();
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
    }

}
