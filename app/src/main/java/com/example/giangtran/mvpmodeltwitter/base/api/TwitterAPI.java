package com.example.giangtran.mvpmodeltwitter.base.api;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by giangtran on 16/10/2017.
 */

public class TwitterAPI extends OAuthBaseClient {
    public static final BaseApi REST_API_CLASS = TwitterApi.instance();
    public static final String REST_URL = "https://api.twitter.com/1.1";
    public static final String REST_CONSUMER_KEY = "c47dt2UNyuMr9m4KlHYxllVuN";
    public static final String REST_CONSUMER_SECRET = "9etpnOczy9AQ2yeFUaLkCzCycB86zt48WSfnCuusb4iqrDnX4y";
    public static final String REST_CALLBACK_URL = "oauth://twitterreviewapp";

    public TwitterAPI(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    public void getHomeTimeline(int since_id,JsonHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count",25);
        params.put("since_id",since_id);
        getClient().get(apiUrl, params, handler);
    }

    public void getMyUserProfile(JsonHttpResponseHandler handler){
        String apiUrl = getApiUrl("users/show.json?screen_name=lingboo2408");
        getClient().get(apiUrl,handler);
    }
    public void postUpdateStatus(String query, JsonHttpResponseHandler handler){
        String apiUrl = null;
        try {
            apiUrl = getApiUrl("statuses/update.json?status="+ URLEncoder.encode(query, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getClient().post(apiUrl, handler);
    }
    public void postFavorited(long id,JsonHttpResponseHandler handler){
        String apiUrl = getApiUrl("favorites/create.json?id="+id);
        getClient().post(apiUrl,handler);
    }
    public void postRetweeted(long id,JsonHttpResponseHandler handler){
        String apiUrl = getApiUrl("statuses/retweet/"+id+".json");
        getClient().post(apiUrl,handler);
    }
}
