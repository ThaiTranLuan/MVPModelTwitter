package com.example.giangtran.mvpmodeltwitter.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.example.giangtran.mvpmodeltwitter.main.view.fragment.ComposeFragment;
import com.loopj.android.http.HttpGet;

import java.io.File;

import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by giangtran on 19/10/2017.
 */

public class Common {
    private Activity activity;
    private Context context;
    public Common(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    public Common(Context context){
        this.context = context;
    }
    public void callFragment(){
        FragmentActivity fa = (FragmentActivity) activity;
        FragmentManager fm = fa.getSupportFragmentManager();
        ComposeFragment cf = ComposeFragment.newInstace(null);
        cf.show(fm,"composefragmnet");
    }
    public static void isNetWorkAvailable(final Handler handler, final int timeout){
        // ask fo message '0' (not connected) or '1' (connected) on 'handler'
        // the answer must be send before before within the 'timeout' (in milliseconds)

        new Thread() {
            private boolean responded = false;
            @Override
            public void run() {
                // set 'responded' to TRUE if is able to connect with google mobile (responds fast)
                new Thread() {
                    @Override
                    public void run() {
                        HttpGet requestForTest = new HttpGet("http://m.google.com");
                        try {
                            new DefaultHttpClient().execute(requestForTest); // can last...
                            responded = true;
                        }
                        catch (Exception e) {
                        }
                    }
                }.start();

                try {
                    int waited = 0;
                    while(!responded && (waited < timeout)) {
                        sleep(100);
                        if(!responded ) {
                            waited += 100;
                        }
                    }
                }
                catch(InterruptedException e) {} // do nothing
                finally {
                    if (!responded) { handler.sendEmptyMessage(0); }
                    else { handler.sendEmptyMessage(1); }
                }
            }
        }.start();
    }
    public void galleryIntents() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (intent.resolveActivity(activity.getPackageManager()) != null)
            activity.startActivityForResult(Intent.createChooser(intent, "Select File"),Constant.SELECT_FILE);
    }
    private void captureIntents() {
        File directory = new File(Environment.getExternalStorageDirectory() + "/Twitter");
        if (!directory.exists()){
            directory.mkdir();
        }
        String fileImage = System.currentTimeMillis() + ".png";
        File outputFileCamera = new File(directory,fileImage);
        Uri outputUriCamera = Uri.fromFile(outputFileCamera);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,outputUriCamera);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(activity.getPackageManager()) != null){
            activity.startActivityForResult(intent, Constant.REQUEST_CAMERA);
        }
    }

}
