package com.example.giangtran.mvpmodeltwitter.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.codepath.oauth.OAuthLoginActionBarActivity;
import com.example.giangtran.mvpmodeltwitter.R;
import com.example.giangtran.mvpmodeltwitter.base.api.TwitterAPI;
import com.example.giangtran.mvpmodeltwitter.main.timeline.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by giangtran on 16/10/2017.
 */

public class LoginActivity extends OAuthLoginActionBarActivity<TwitterAPI> {
    @BindView(R.id.btnLogin)
    Button btnClick;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getClient().connect();
            }
        });
    }

    @Override
    public void onLoginSuccess() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
}
