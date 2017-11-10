package com.example.giangtran.mvpmodeltwitter.main.timeline;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giangtran.mvpmodeltwitter.base.model.Tweet;
import com.example.giangtran.mvpmodeltwitter.base.utils.Constant;
import com.example.giangtran.mvpmodeltwitter.base.utils.ExitApplication;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.app.Activity.RESULT_OK;


/**
 * Created by giangtran on 25/10/2017.
 */

public class TimelinePresenterImpl implements ITimelinePresenter, ITimelineDataInteractor.OnResponseListener{

    ITimeLineView timeLineView;
    ITimelineDataInteractor fetchData;
    private Context context;
    private Realm myRealm;
    public TimelinePresenterImpl(ITimeLineView timeLineView, ITimelineDataInteractor fetchData,Context context) {
        this.timeLineView = timeLineView;
        this.fetchData = fetchData;
        this.context = context;
        myRealm = Realm.getInstance(this.context);
    }
    int view;

    public void setView(int view){
        this.view = view;
    }


    @Override
    public void onResume() {
        fetchData.loadData(this);
    }

    @Override
    public void onDestroy() {
        timeLineView = null;
    }

    @Override
    public void onItemClicked(Tweet tweet) {
        Log.d("Tweet",tweet.getBody());
    }

    @Override
    public void onPostTweet(String t) {
        fetchData.postData(t,this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.SELECT_FILE){
            if (resultCode == RESULT_OK && data != null){
                final Uri uri = data.getData();
                Log.d("UriImage",uri.toString());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
                        Bitmap bm = null;
                        try {
                            bm = Glide.with(context).asBitmap().load(uri).submit().get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        try {
                            wallpaperManager.setBitmap(bm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }else{
                Toast.makeText(context, "There is error while getting data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onLoadLocal() {
        RealmResults<Tweet> tweetRealmResults = myRealm.where(Tweet.class).findAll();
        Log.d("SizeRealm",tweetRealmResults.size()+"");
        if (timeLineView != null){
            timeLineView.setItems(tweetRealmResults);
        }
    }

    @Override
    public void onSuccess(List<Tweet> tweets) {
        if (timeLineView != null){
            timeLineView.setItems(tweets);
            timeLineView.updateItem();

            myRealm.beginTransaction();
            RealmResults<Tweet> result = myRealm.where(Tweet.class).findAll();
            result.clear();
            for (Tweet t : tweets) {
                myRealm.copyToRealm(t);
            }
            myRealm.commitTransaction();
        }

    }

    @Override
    public void onFailed() {
        timeLineView.setItemsError();
    }

}
