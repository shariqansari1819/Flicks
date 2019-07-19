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
    private static SharedPreferences sharedPreferences;

    public static int appAlive = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        flicksApplication = this;

        AndroidNetworking.initialize(this);
        PRDownloader.initialize(getApplicationContext());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(flicksApplication);
        MobileAds.initialize(this, getResources().getString(R.string.testing_admob_id));

    }

    public static FlicksApplication getAppContext() {
        return flicksApplication;
    }

    public static void putFloatValue(String key, float value) {
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public static void putStringValue(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static void putBooleanValue(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static void putIntValue(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static float getFloatValue(String key) {
        return sharedPreferences.getFloat(key, 0.0f);
    }

    public static int getIntValue(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public static boolean getBooleanValue(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public static String getStringValue(String key) {
        return sharedPreferences.getString(key, "");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
