package com.v0k0.reddittopposts.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.v0k0.reddittopposts.Adapters.PostAdapter;
import com.v0k0.reddittopposts.Network.JSONParser;
import com.v0k0.reddittopposts.Network.RedditConnector;
import com.v0k0.reddittopposts.R;
import com.v0k0.reddittopposts.pojo.PostItem;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class FeedActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {

    private static final String LIST_KEY = "list_key";
    private static final String KEY_BIG_IMAGE = "big_image";
    private static final String PAGE_NUMBER_KEY = "page_num";
    private static final String KEY_URL = "url";
    private static final int LOADER_ID = 1;

    private ImageView imageViewNextPage;
    private ImageView imageViewPreviousPage;
    private ProgressBar progressBar;

    private ArrayList<PostItem> posts = new ArrayList<>();
    private PostAdapter adapter;
    private RecyclerView recyclerView;
    private static final String LIST_STATE = "list_state";
    private static final String RECYCLER_LAYOUT = "recycler_layout";
    private Parcelable savedRecyclerView;

    private int currentPageNumber;
    private LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        recyclerView = findViewById(R.id.recyclerViewPosts);
        imageViewNextPage = findViewById(R.id.btn_get_forward);
        imageViewPreviousPage = findViewById(R.id.btn_get_back);
        progressBar = findViewById(R.id.progressBarLoadingPosts);

        loaderManager = LoaderManager.getInstance(this);

        adapter = new PostAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        if (savedInstanceState != null) {
            posts = savedInstanceState.getParcelableArrayList(LIST_STATE);
            currentPageNumber = savedInstanceState.getInt(PAGE_NUMBER_KEY);
            adapter.setPosts(posts);
            savedRecyclerView = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
            restoreLayoutManager();
        } else {
            posts = getIntent().getParcelableArrayListExtra(LIST_KEY);
            currentPageNumber = getIntent().getIntExtra(PAGE_NUMBER_KEY, 0);
            adapter.setPosts(posts);
        }

        recyclerView.setAdapter(adapter);
        adapter.setOnPostImageClickListener(onPostImageClickListener);

        imageViewNextPage.setOnClickListener(onNextPageClickListener);
        imageViewPreviousPage.setOnClickListener(onPreviousPageClick);

    }

    private void restoreLayoutManager() {
        if (savedRecyclerView != null) {
            Objects.requireNonNull(recyclerView.getLayoutManager()).onRestoreInstanceState(savedRecyclerView);
        }
    }

    private ImageView.OnClickListener onNextPageClickListener = (view) -> {
        String lastPostId = posts.get(posts.size() - 1).getPostId();
        currentPageNumber++;
        downloadPosts(true, lastPostId);
    };

    private ImageView.OnClickListener onPreviousPageClick = (view) -> {
        if (currentPageNumber > 0) {
            currentPageNumber--;
            String firstPostId = posts.get(0).getPostId();
            downloadPosts(false, firstPostId);
        }
    };

    private void downloadPosts(boolean isAfter, String postId){
        Bundle bundle = new Bundle();
        URL url = RedditConnector.buildUrl(isAfter, postId);
        if (url != null){
            bundle.putString(KEY_URL, url.toString());
            loaderManager.restartLoader(LOADER_ID, bundle ,this);
        }
    }

    private PostAdapter.OnPostImageClickListener onPostImageClickListener = position -> {
        PostItem post = posts.get(position);
        String bigImagePath = post.getBigPicturePath();
        if (bigImagePath.endsWith(".jpg") || bigImagePath.endsWith(".png")) {
            Intent intent = new Intent(FeedActivity.this, FullScreenActivity.class);
            intent.putExtra(KEY_BIG_IMAGE, bigImagePath);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.no_big_picture_warning), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE, posts);
        outState.putInt(PAGE_NUMBER_KEY, currentPageNumber);
        outState.putParcelable(RECYCLER_LAYOUT, Objects.requireNonNull(recyclerView.getLayoutManager()).onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        posts = savedInstanceState.getParcelableArrayList(LIST_STATE);
        savedRecyclerView = savedInstanceState.getParcelable(RECYCLER_LAYOUT);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        RedditConnector.JSONLoader jsonLoader = new RedditConnector.JSONLoader(this,args);
        progressBar.setVisibility(View.VISIBLE);
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        progressBar.setVisibility(View.INVISIBLE);
        posts = JSONParser.getPostsFromJSON(data);
        loaderManager.destroyLoader(LOADER_ID);
        restartActivityWithNewPosts();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }

    private void restartActivityWithNewPosts(){
        if (posts.size() != 0){
            Intent intent = new Intent(FeedActivity.this, FeedActivity.class);
            intent.putParcelableArrayListExtra(LIST_KEY, posts);
            intent.putExtra(PAGE_NUMBER_KEY, currentPageNumber);
            startActivity(intent);
            finish();
        }
    }
}
