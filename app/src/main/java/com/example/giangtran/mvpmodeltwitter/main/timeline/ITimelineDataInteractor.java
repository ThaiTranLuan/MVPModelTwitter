package com.example.giangtran.mvpmodeltwitter.main.timeline;

import android.content.Context;

import com.example.giangtran.mvpmodeltwitter.base.model.Tweet;

import java.util.List;

/**
 * Created by giangtran on 25/10/2017.
 */

public interface ITimelineDataInteractor {
    interface OnResponseListener{
        void onSuccess(List<Tweet> tweets);
        void onFailed();
    }
    void loadData(OnResponseListener listener);
    void postData(String text,OnResponseListener listener);
}
