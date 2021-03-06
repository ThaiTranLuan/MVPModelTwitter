package com.example.giangtran.mvpmodeltwitter.base.application;

import android.app.Application;
import android.content.Context;

import com.example.giangtran.mvpmodeltwitter.base.api.TwitterAPI;

import io.realm.Realm;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 *
 *     // use client to send requests to API
 *
 */
public class TwitterApplication extends Application {
	private static Context context;
	@Override
	public void onCreate() {
		super.onCreate();
		TwitterApplication.context = this;
	}

	public static TwitterAPI getRestClient() {
		return (TwitterAPI) TwitterAPI.getInstance(TwitterAPI.class, TwitterApplication.context);
	}
}