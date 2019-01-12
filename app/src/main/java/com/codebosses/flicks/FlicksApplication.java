package com.codebosses.flicks;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
    }

    public static FlicksApplication getAppContext() {
        return flicksApplication;
    }
}
