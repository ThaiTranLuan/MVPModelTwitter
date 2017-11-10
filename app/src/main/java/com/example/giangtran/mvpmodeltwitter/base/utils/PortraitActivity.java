package com.example.giangtran.mvpmodeltwitter.base.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;


/**
 * Created by giangtran on 19/10/2017.
 */

public class PortraitActivity extends AppCompatActivity implements IConnectivityListener {

    protected Context context;
    protected static Snackbar sn;
    private BroadcastReceiver br;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Handler h = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1){
                    onConnect();
                }else{
                    onDisconnect();
                }
            }
        };

//        Common.isNetWorkAvailable(h,2000);
        checkNetworkDuringRuntime();
    }

    void checkNetworkDuringRuntime(){
        br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isConnected()){
                    onConnect();
                }else{
                    onDisconnect();
                }
            }
        };
        registerReceiver(br,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }
    public boolean isConnected() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { Log.e("ERROR", "IOException",e); }
        catch (InterruptedException e) { Log.e("ERROR", "InterruptedException",e); }

        return false;
    }

    protected void createSnackbar(View view){
        sn = Snackbar.make(view,"No Network", Snackbar.LENGTH_INDEFINITE);
    }

    @Override
    public void onConnect() {
        if (sn != null)
            sn.dismiss();
        Toast.makeText(context, "YES!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisconnect() {
        sn.show();
        Toast.makeText(context, "NO!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (br != null)
            unregisterReceiver(br);
    }
}
