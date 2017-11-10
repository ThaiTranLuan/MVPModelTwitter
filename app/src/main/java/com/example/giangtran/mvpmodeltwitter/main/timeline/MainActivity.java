package com.example.giangtran.mvpmodeltwitter.main.timeline;


import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giangtran.mvpmodeltwitter.base.model.Tweet;
import com.example.giangtran.mvpmodeltwitter.base.utils.Common;
import com.example.giangtran.mvpmodeltwitter.base.utils.Constant;
import com.example.giangtran.mvpmodeltwitter.base.utils.PortraitActivity;
import com.example.giangtran.mvpmodeltwitter.R;
import com.example.giangtran.mvpmodeltwitter.main.view.fragment.ComposeFragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends PortraitActivity implements ITimeLineView,ComposeFragment.OnPassDataToActivity,TimelineAdapter.OnChangingActivity,SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.rvTimeline)
    RecyclerView rvTimeline;
    @BindView(R.id.srSwipe)
    SwipeRefreshLayout swipe;
    private TimelinePresenterImpl tl;
    private TimelineAdapter adapter;
    Common cm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        createSnackbar(swipe);
        context = getApplicationContext();
        cm = new Common(getApplicationContext(), this);
        swipe.setOnRefreshListener(this);
        tl = new TimelinePresenterImpl(this, new TimelineDataInteractorImpl(),context);
        tl.setView(R.id.srSwipe);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        tl.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compose:
//                cm.callFragment();
                cm.galleryIntents();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        tl.onActivityResult(requestCode,resultCode,data);

    }

    @Override
    public void getValue(String text) {
        tl.onPostTweet(text);
    }

    @Override
    public void setItems(List<Tweet> tweet) {
        adapter = new TimelineAdapter(getApplicationContext(),tweet,this);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        rvTimeline.setLayoutManager(lm);
        rvTimeline.setAdapter(adapter);
    }

    @Override
    public void setItemsError() {
        Toast.makeText(context, "Load item failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateItem() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPassingData(Tweet tweet) {
        tl.onItemClicked(tweet);
    }

    @Override
    public void onRefresh() {
        tl.onResume();
        swipe.setRefreshing(false);
    }

    @Override
    public void onDisconnect() {
        super.onDisconnect();
        tl.onLoadLocal();
    }

    @Override
    public void onConnect() {
        super.onConnect();
        tl.onResume();
    }
}
