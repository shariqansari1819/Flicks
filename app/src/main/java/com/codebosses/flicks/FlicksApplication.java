package com.codebosses.flicks;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.ads.MobileAds;

public class FlicksApplication extends Application {

    private static FlicksApplication flicksApplication;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        flicksApplication = this;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(flicksApplication);
        editor = sharedPreferences.edit();
        MobileAds.initialize(this, getResources().getString(R.string.admob_id));
    }

    public static FlicksApplication getAppContext() {
        return flicksApplication;
    }
}
