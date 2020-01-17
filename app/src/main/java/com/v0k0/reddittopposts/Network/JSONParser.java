package com.v0k0.reddittopposts.Network;

import com.v0k0.reddittopposts.pojo.PostItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {

    private static final String KEY_THUMBNAIL = "thumbnail";
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_COMMENTS_COUNT = "num_comments";
    private static final String KEY_UNIX_TIME = "created_utc";
    private static final String KEY_POST_ID = "name";

    private static final String KEY_DATA = "data";
    private static final String KEY_CHILDREN = "children";


    public static ArrayList<PostItem> getPostsFromJSON(JSONObject jsonObject){
        ArrayList<PostItem> result = new ArrayList<>();
        try {
            JSONObject dataJSON = jsonObject.getJSONObject(KEY_DATA);
            JSONArray jsonArray = dataJSON.getJSONArray(KEY_CHILDREN);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonPost = jsonArray.getJSONObject(i).getJSONObject(KEY_DATA);
                String author = jsonPost.getString(KEY_AUTHOR);
                int commentsCount = jsonPost.getInt(KEY_COMMENTS_COUNT);
                long unix_time = jsonPost.getLong(KEY_UNIX_TIME);
                String id = jsonPost.getString(KEY_POST_ID);
                String thumbnalPath = jsonPost.getString(KEY_THUMBNAIL);
                PostItem post = new PostItem(id, author, commentsCount, unix_time, thumbnalPath);
                result.add(post);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
         return result;
    }



}
