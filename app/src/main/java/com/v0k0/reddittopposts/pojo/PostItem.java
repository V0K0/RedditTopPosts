package com.v0k0.reddittopposts.pojo;

import android.icu.util.LocaleData;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class PostItem implements Parcelable {

    private String author;
    private int commentsCount;
    private long unix_time;
    private String thumbnailPath;
    private String bigPicturePath;
    private String postId;

    public PostItem(String postId, String author, int commentsCount, long unix_time, String thumbnailPath, String bigPicturePath) {
        this.author = author;
        this.commentsCount = commentsCount;
        this.unix_time = unix_time;
        this.thumbnailPath = thumbnailPath;
        this.bigPicturePath = bigPicturePath;
        this.postId = postId;
    }

    private PostItem(Parcel in) {
        author = in.readString();
        commentsCount = in.readInt();
        unix_time = in.readLong();
        thumbnailPath = in.readString();
        bigPicturePath = in.readString();
        postId = in.readString();
    }

    public static final Creator<PostItem> CREATOR = new Creator<PostItem>() {
        @Override
        public PostItem createFromParcel(Parcel in) {
            return new PostItem(in);
        }

        @Override
        public PostItem[] newArray(int size) {
            return new PostItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeInt(commentsCount);
        dest.writeLong(unix_time);
        dest.writeString(thumbnailPath);
        dest.writeString(bigPicturePath);
        dest.writeString(postId);
    }

    public String getAuthor() {
        return author;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public String getBigPicturePath() {
        return bigPicturePath;
    }

    public long getUnix_time() {
        return unix_time;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }


    public String getTimeOfPost() {
        return convertUnixInDate();
    }

    public String getPostId() {
        return postId;
    }

    private String convertUnixInDate() {
        Date postTime = new Date(unix_time * 1000L);
        Date current = new Date();
        long hours = TimeUnit.MILLISECONDS.toHours(current.getTime() - postTime.getTime());

        return hours >= 24 ? "over a day ago" : hours == 1 ? "an hour ago" : hours + " hours ago";
    }

}
