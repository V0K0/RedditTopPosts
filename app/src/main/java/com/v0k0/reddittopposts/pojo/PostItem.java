package com.v0k0.reddittopposts.pojo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PostItem {

    private String author;
    private int commentsCount;
    private long unix_time;
    private String date;
    private String picturePath;


    public PostItem(String author, int commentsCount, long unix_time, String picturePath) {
        this.author = author;
        this.commentsCount = commentsCount;
        this.unix_time = unix_time;
        this.picturePath = picturePath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public long getUnix_time() {
        return unix_time;
    }

    public void setUnix_time(long unix_time) {
        this.unix_time = unix_time;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getTimeOfPost(){
       return convertUnixInDate();
    }

    private String convertUnixInDate(){
        Date date = new Date(unix_time * 1000L);
        String timePattern = "h:mm a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(timePattern, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        return dateFormat.format(date);
    }

}
