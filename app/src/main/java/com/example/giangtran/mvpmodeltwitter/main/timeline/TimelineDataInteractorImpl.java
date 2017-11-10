package com.example.giangtran.mvpmodeltwitter.main.timeline;

import android.content.Context;
import android.util.Log;

import com.example.giangtran.mvpmodeltwitter.base.api.TwitterAPI;
import com.example.giangtran.mvpmodeltwitter.base.application.TwitterApplication;
import com.example.giangtran.mvpmodeltwitter.base.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by giangtran on 25/10/2017.
 */

public class TimelineDataInteractorImpl implements ITimelineDataInteractor {
    TwitterAPI api = TwitterApplication.getRestClient();
    List<Tweet> templTweet = new ArrayList<>();
    @Override
    public void loadData(OnResponseListener responseListener) {
       getListTweet(responseListener);
    }

    @Override
    public void postData(String text, final OnResponseListener listener) {
        api.postUpdateStatus(text, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                templTweet.add(0,Tweet.fromJSON(response));
                listener.onSuccess(templTweet);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.onFailed();
            }
        });
    }

    private List<Tweet> getListTweet(final OnResponseListener listener){
        if (templTweet != null){
            templTweet.clear();
        }

        api.getHomeTimeline(1, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                templTweet.addAll(Tweet.fromJSONArray(response));
                listener.onSuccess(templTweet);
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                listener.onFailed();
            }
        });
        return templTweet;
    }
}
