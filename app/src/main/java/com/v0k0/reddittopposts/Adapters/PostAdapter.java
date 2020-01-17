package com.v0k0.reddittopposts.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.v0k0.reddittopposts.R;
import com.v0k0.reddittopposts.pojo.PostItem;


import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<PostItem> posts;

    public List<PostItem> getPosts() {
        return posts;
    }

    public void setPosts(List<PostItem> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    public PostAdapter() {
        this.posts = new ArrayList<>();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reddit_post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostItem post = posts.get(position);
        String comments = post.getCommentsCount() + " comments";
        Picasso.get().load(post.getPicturePath()).into(holder.imageViewPoster);
        holder.textViewAuthor.setText(post.getAuthor());
        holder.textViewComments.setText(comments);
        holder.textViewDate.setText(post.getTimeOfPost());
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewAuthor;
        private TextView textViewDate;
        private TextView textViewComments;
        private ImageView imageViewPoster;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewComments = itemView.findViewById(R.id.textViewComments);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            imageViewPoster = itemView.findViewById(R.id.imageViewPostPicture);
        }
    }
}
