package com.v0k0.reddittopposts.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PostItem {

    private String author;
    private int commentsCount;
    private long unix_time;
    private String picturePath;
    private String postId;

    public PostItem(String postId,String author, int commentsCount, long unix_time, String picturePath) {
        this.author = author;
        this.commentsCount = commentsCount;
        this.unix_time = unix_time;
        this.picturePath = picturePath;
        this.postId = postId;
    }

    public String getAuthor() {
        return author;
    }

    public int getCommentsCount() {
        return commentsCount;
    }


    public long getUnix_time() {
        return unix_time;
    }

    public String getPicturePath() {
        return picturePath;
    }


    public String getTimeOfPost(){
       return convertUnixInDate();
    }

    public String getPostId() {
        return postId;
    }

    private String convertUnixInDate(){
        Date date = new Date(unix_time * 1000L);
        String timePattern = "h:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(timePattern, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        return dateFormat.format(date);
    }

}
