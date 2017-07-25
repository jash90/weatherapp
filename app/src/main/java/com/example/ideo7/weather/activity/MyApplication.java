package com.example.ideo7.weather.activity;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ideo7 on 14.07.2017.
 */

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
       // XLog.init(LogLevel.ALL);
    }
}