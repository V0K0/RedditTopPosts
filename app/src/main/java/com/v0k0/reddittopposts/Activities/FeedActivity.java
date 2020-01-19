package com.v0k0.reddittopposts.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.v0k0.reddittopposts.Adapters.PostAdapter;
import com.v0k0.reddittopposts.R;
import com.v0k0.reddittopposts.pojo.PostItem;

import java.util.ArrayList;
import java.util.Objects;

public class FeedActivity extends AppCompatActivity {

    private static final String LIST_KEY = "list_key";
    private static final String KEY_BIG_IMAGE = "big_image";

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
            posts = getIntent().getParcelableArrayListExtra(LIST_KEY);
            adapter.setPosts(posts);
        }
        recyclerView.setAdapter(adapter);
        adapter.setOnPostImageClickListener(onPostImageClickListener);
    }

    private void restoreLayoutManager(){
        if (savedRecyclerView != null){
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecyclerView);
        }
    }

    private PostAdapter.OnPostImageClickListener onPostImageClickListener = position -> {
        PostItem post = posts.get(position);
        String bigImagePath = post.getBigPicturePath();
        if (bigImagePath.contains(".jpg") || bigImagePath.contains(".png")) {
            Intent intent = new Intent(FeedActivity.this, FullScreenActivity.class);
            intent.putExtra(KEY_BIG_IMAGE, bigImagePath);
            startActivity(intent);
        } else {
            Toast.makeText(this, "There isn`t big picture", Toast.LENGTH_SHORT).show();
        }
    };


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
