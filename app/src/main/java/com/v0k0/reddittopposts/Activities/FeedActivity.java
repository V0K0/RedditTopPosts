package com.v0k0.reddittopposts.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.v0k0.reddittopposts.Adapters.PostAdapter;
import com.v0k0.reddittopposts.Network.JSONParser;
import com.v0k0.reddittopposts.Network.RedditConnector;
import com.v0k0.reddittopposts.R;
import com.v0k0.reddittopposts.pojo.PostItem;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    private boolean isFirstQuerry = true;
    private ArrayList<PostItem> posts = new ArrayList<>();
    private PostAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        getTopPostsFromReddit();
    }

    private void getTopPostsFromReddit() {
        if (isFirstQuerry) {
            posts = JSONParser.getPostsFromJSON(RedditConnector.getJSONFromReddit(""));
            isFirstQuerry = false;
        } else {
            String afterPost = posts.get(posts.size() - 1).getPostId();
            posts.clear();
            posts = JSONParser.getPostsFromJSON(RedditConnector.getJSONFromReddit(afterPost));
        }
    }


}
