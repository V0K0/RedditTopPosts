package com.v0k0.reddittopposts.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.v0k0.reddittopposts.Adapters.PostAdapter;
import com.v0k0.reddittopposts.Network.JSONParser;
import com.v0k0.reddittopposts.Network.RedditConnector;
import com.v0k0.reddittopposts.R;
import com.v0k0.reddittopposts.pojo.PostItem;

import java.util.ArrayList;
import java.util.Objects;

public class FeedActivity extends AppCompatActivity {

    private boolean isFirstQuerry = true;
    private ArrayList<PostItem> posts = new ArrayList<>();
    private PostAdapter adapter;
    private RecyclerView recyclerView;
    private static final String LIST_STATE = "list_state";
    private static final String RECYCLER_LAYOUT = "recycler_layout";
    private Parcelable savedRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        recyclerView = findViewById(R.id.recyclerViewPosts);
        adapter = new PostAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(savedInstanceState != null){
            posts = savedInstanceState.getParcelableArrayList(LIST_STATE);
            adapter.setPosts(posts);
            savedRecyclerView = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
            restoreLayoutManager();
        } else {
            getTopPostsFromReddit();
            adapter.setPosts(posts);
        }
        recyclerView.setAdapter(adapter);

    }

    private void restoreLayoutManager(){
        if (savedRecyclerView != null){
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecyclerView);
        }
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
        if (!posts.isEmpty()){
            Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE, posts);
        outState.putParcelable(RECYCLER_LAYOUT, Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        posts = savedInstanceState.getParcelableArrayList(LIST_STATE);
        savedRecyclerView = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
    }


}
