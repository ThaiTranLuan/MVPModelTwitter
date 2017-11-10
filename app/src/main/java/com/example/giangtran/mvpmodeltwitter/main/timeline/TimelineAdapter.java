package com.example.giangtran.mvpmodeltwitter.main.timeline;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giangtran.mvpmodeltwitter.base.utils.Common;
import com.example.giangtran.mvpmodeltwitter.R;
import com.example.giangtran.mvpmodeltwitter.base.api.TwitterAPI;
import com.example.giangtran.mvpmodeltwitter.base.application.TwitterApplication;
import com.example.giangtran.mvpmodeltwitter.base.model.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by giangtran on 16/10/2017.
 */

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineHolder> {

    private Context context;
    private List<Tweet> mTweets;
    OnChangingActivity onChangingActivity;
    public TimelineAdapter(Context context, List<Tweet> list,OnChangingActivity oc){
        this.context = context;
        mTweets = list;
        onChangingActivity = oc;
    }

    public interface OnChangingActivity{
        void onPassingData(Tweet tweet);
    }


    @Override
    public TimelineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater ly = LayoutInflater.from(context);
        View tweetView = ly.inflate(R.layout.item_tweet,parent,false);
        ButterKnife.bind(this,tweetView);
        TimelineHolder holder = new TimelineHolder(tweetView,mTweets,onChangingActivity);
        return holder;
    }

    @Override
    public void onBindViewHolder(TimelineHolder holder, int position) {
        final Tweet tweet = mTweets.get(position);
        holder.tvUsername.setText(tweet.getUser().getScreenName());
        holder.tvBody.setText(tweet.getBody());
        holder.tvScreenName.setText("@"+tweet.getUser().getScreenName());
        holder.tvRelativeTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        holder.tvFavoriteCount.setText(""+tweet.getFavoriteCount());
        holder.tvRetweetCount.setText(""+tweet.getRetweetCount());
        if (tweet.getRetweeted().equals("true")){
            holder.ivRetweetCount.setImageResource(R.drawable.retweetclick);
            holder.tvRetweetCount.setTextColor(R.color.retweet_click);
        }else holder.ivRetweetCount.setImageResource(R.drawable.retweet);
        if (tweet.getFavorited().equals("true")){
            holder.ivFavouritesCount.setImageResource(R.drawable.starclick);
            holder.tvFavoriteCount.setTextColor(R.color.favorite_click);
        }else holder.ivFavouritesCount.setImageResource(R.drawable.star);
        Glide.with(context).load(tweet.getUser().getProfileImageUrl()).into(holder.ivProfileImage);
        Glide.with(context).load(tweet.getUrlImageNews()).into(holder.ivNewsImage);
        clickReply(holder,tweet);
        clickRetweet(holder,tweet);
        clickFavorite(holder,tweet);
    }
    public void clickReply(TimelineHolder holder, final Tweet tweet) {
        holder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof FragmentActivity) {
                    Common cm = new Common(context);
                    cm.callFragment();
                }
            }
        });
    }
    public static void clickRetweet(final TimelineHolder holder, final Tweet tweet) {
        holder.ivRetweetCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterAPI client = TwitterApplication.getRestClient();
                client.postRetweeted(tweet.getUid(),new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        holder.tvRetweetCount.setText(""+(tweet.getRetweetCount() + 1));
                        holder.ivRetweetCount.setImageResource(R.drawable.retweetclick);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        });
    }
    public static void clickFavorite(final TimelineHolder holder, final Tweet tweet) {
        holder.ivFavouritesCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterAPI client = TwitterApplication.getRestClient();
                client.postFavorited(tweet.getUid(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                        holder.tvFavoriteCount.setText("" + (tweet.getFavoriteCount() + 1));
                        holder.ivFavouritesCount.setImageResource(R.drawable.starclick);
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTweets == null ? 0 : mTweets.size();
    }

    class TimelineHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnChangingActivity oc;
        @BindView(R.id.ivProfileImage)
        ImageView ivProfileImage;
        @BindView(R.id.ivImageNews)
        ImageView ivNewsImage;
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvBody)
        TextView tvBody;
        @BindView(R.id.tvScreenName)
        TextView tvScreenName;
        @BindView(R.id.tvRelativeTime)
        TextView tvRelativeTime;
        @BindView(R.id.tvFavoriteCount)
        TextView tvFavoriteCount;
        @BindView(R.id.tvRetweetCount)
        TextView tvRetweetCount;
        @BindView(R.id.ivFavouritesCount)
        ImageView ivFavouritesCount;
        @BindView(R.id.ivRetweetCount)
        ImageView ivRetweetCount;
        @BindView(R.id.ivReply)
        ImageView ivReply;
        private Context context;
        private List<Tweet> tweets;
        public TimelineHolder(View itemView,List<Tweet> tweets,OnChangingActivity onChangingActivity) {
            super(itemView);
            this.tweets = tweets;
            oc = onChangingActivity;
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Animation animation = new AlphaAnimation(0.3f,1.0f);
                    v.startAnimation(animation);
                    return false;
                }
            });
        }

        @Override
        public void onClick(View view) {
            Animation animation = new AlphaAnimation(0.3f,1.0f);
            animation.setDuration(1000);
            view.startAnimation(animation);
            oc.onPassingData(tweets.get(getAdapterPosition()));

        }
    }
    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
