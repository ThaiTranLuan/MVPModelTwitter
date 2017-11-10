package com.example.giangtran.mvpmodeltwitter.main.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giangtran.mvpmodeltwitter.R;
import com.example.giangtran.mvpmodeltwitter.base.api.TwitterAPI;
import com.example.giangtran.mvpmodeltwitter.base.application.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Boo on 3/26/2016.
 */
public class ComposeFragment extends DialogFragment implements View.OnClickListener {
    TwitterAPI client;
    @BindView(R.id.ivProfileImageFragment)
    ImageView ivProfileImage;
    @BindView(R.id.edtCompose)
    EditText edtCompose;
    @BindView(R.id.tvUsernameFragment)
    TextView tvUsername;
    @BindView(R.id.tvScreenNameFragment)
    TextView tvScreenName;
    @BindView(R.id.tvCountText)
    TextView tvCountText;
    @BindView(R.id.ibClose)
    ImageButton ibClose;
    @BindView(R.id.scroll)
    ScrollView scrollView;
    @BindView(R.id.button)
    Button btnTweet;

    public interface OnPassDataToActivity{
        void getValue(String text);
    }

    OnPassDataToActivity onPassDataToActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onPassDataToActivity = (OnPassDataToActivity) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onPassDataToActivity");
        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvCountText.setText(String.valueOf(140-s.length()));
            boolean a = 140 - s.length() < 0 || 140 - s.length() > 140 ? false : true;
            btnTweet.setEnabled(a);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public ComposeFragment(){

    }
    public static ComposeFragment newInstace(String screen){
        ComposeFragment composeFragment = new ComposeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("screen_name", screen);
        composeFragment.setArguments(bundle);
        return composeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_compose, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        String screenName = getArguments().getString("screen_name");
        setupViews(v);
        if(screenName!=null){
            edtCompose.setText("@"+screenName);
        }else edtCompose.setText("");
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        populateProfile();
    }

    private void setupViews(View view) {
        ButterKnife.bind(this,view);
        btnTweet.setOnClickListener(this);
//        scrollView.setEnabled(false);
        client = TwitterApplication.getRestClient();
    }

    private void populateProfile(){
        client.getMyUserProfile(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                Log.d("DEBUG",response.toString());
                try {
                    String stringImage = response.getString("profile_image_url");
                    String stringScreenName = response.getString("screen_name");
                    String stringName = response.getString("name");
                    tvUsername.setText(stringName);
                    tvScreenName.setText("@"+stringScreenName+": ");
                    Glide.with(getContext()).load(stringImage).into(ivProfileImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }
    @Override
    public void onClick(View view) {
        String s = edtCompose.getText().toString();
        onPassDataToActivity.getValue(s);
        dismiss();
    }
}
