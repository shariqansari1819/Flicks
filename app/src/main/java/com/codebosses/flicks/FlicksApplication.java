package com.codebosses.flicks;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.androidnetworking.AndroidNetworking;
import com.downloader.PRDownloader;
import com.google.android.gms.ads.MobileAds;

import androidx.multidex.MultiDex;

public class FlicksApplication extends Application {

    private static FlicksApplication flicksApplication;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    public static int appAlive = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        flicksApplication = this;


        AndroidNetworking.initialize(this);
        PRDownloader.initialize(getApplicationContext());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(flicksApplication);
        editor = sharedPreferences.edit();
        MobileAds.initialize(this, getResources().getString(R.string.admob_id));

    }

    public static FlicksApplication getAppContext() {
        return flicksApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
