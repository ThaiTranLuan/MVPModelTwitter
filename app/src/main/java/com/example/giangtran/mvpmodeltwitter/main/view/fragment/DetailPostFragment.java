package com.example.giangtran.mvpmodeltwitter.main.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giangtran.mvpmodeltwitter.base.utils.Common;
import com.example.giangtran.mvpmodeltwitter.R;
import com.example.giangtran.mvpmodeltwitter.base.model.Tweet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by giangtran on 17/10/2017.
 */

public class DetailPostFragment extends Fragment {
    @BindViews({R.id.ivDetailImageProfile,R.id.ivDetailNewsImage,R.id.ivDetailReply,R.id.ivDetailRetweetCount,R.id.ivDetailFavouritesCount})
    ImageView[] imageViews; //ivDetailProfileImage,ivDetailNewsImage,ivDetailReply,ivDetailRetweet, ivDetailFavorite;
    @BindViews({R.id.tvDetailUsername,R.id.tvDetailScreenName,R.id.tvDetailBody,R.id.tvDetailRelativeTime,R.id.tvDetailFavoriteCount,R.id.tvDetailRetweetCount})
    TextView[] textViews;//tvDetailUsername, tvDetailScreenName, tvDetailBody,tvDetailRelativeTime,tvDetailFavoriteCount,tvDetailRetweetCount;
    @BindView(R.id.edtReply) EditText edtReply;
    @BindView(R.id.rl_detail_user)
    RelativeLayout rl;
    Common cm;

    public DetailPostFragment(){
    }
    static DetailPostFragment instance;
    public static DetailPostFragment getInstance(Tweet tweet){
        if(instance != null){
            return instance;
        }
        instance = new DetailPostFragment();
//        Bundle bd = new Bundle();
//        bd.putParcelable("Tweet",tweet);
//        instance.setArguments(bd);

        return instance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_user,container,false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        Tweet tweet = getArguments().getParcelable("Tweet");
        cm = new Common(getContext(),getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
