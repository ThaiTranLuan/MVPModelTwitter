package com.example.giangtran.mvpmodeltwitter.base.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.RealmObject;

/**
 * Created by giangtran on 16/10/2017.
 */

public class Tweet extends RealmObject{
    private String body;
    private long uid;
    private User user;
    private String createdAt;
    private String urlImageNews;
    private int retweetCount;
    private int favoriteCount;
    private String retweeted;
    private String favorited;
    public Tweet() {

    }

    protected Tweet(Parcel in) {
        body = in.readString();
        uid = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        createdAt = in.readString();
        urlImageNews = in.readString();
        retweetCount = in.readInt();
        favoriteCount = in.readInt();
        retweeted = in.readString();
        favorited = in.readString();
    }
    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createdAt =  jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");
            tweet.retweeted = jsonObject.getString("retweeted");
            Log.i("tweet",tweet.retweeted);
            tweet.favorited = jsonObject.getString("favorited");
            Log.i("favorited",tweet.favorited);
            JSONArray media = jsonObject.getJSONObject("entities").optJSONArray("media");
            if (media != null) {
                for(int i = 0 ; i < media.length();i++){
                    JSONObject a = media.getJSONObject(i);
                    if(a.getString("type").equals("photo")){
                        tweet.urlImageNews = a.getString("media_url_https");
                        Log.d("MEDIA", tweet.urlImageNews);
                    }
                }
            }
        } catch (JSONException e) {
            Log.d("DEBUG", "Caught Exception");
            e.printStackTrace();
        }
        return tweet;
    }
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0 ;i <jsonArray.length();i++){
            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if(tweet != null ){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tweets;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUrlImageNews() {
        return urlImageNews;
    }

    public void setUrlImageNews(String urlImageNews) {
        this.urlImageNews = urlImageNews;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(String retweeted) {
        this.retweeted = retweeted;
    }

    public String getFavorited() {
        return favorited;
    }

    public void setFavorited(String favorited) {
        this.favorited = favorited;
    }
}
