package com.example.giangtran.mvpmodeltwitter.main.timeline;

import android.content.Intent;

import com.example.giangtran.mvpmodeltwitter.base.model.Tweet;

import java.util.List;

/**
 * Created by giangtran on 16/10/2017.
 */

public interface ITimelinePresenter {
    void onResume();

    void onDestroy();

    void onItemClicked(Tweet tweet);

    void onPostTweet(String t);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void onLoadLocal();
}
