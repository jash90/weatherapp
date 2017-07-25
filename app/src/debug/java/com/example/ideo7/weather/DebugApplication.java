package com.example.ideo7.weather;

import android.app.Application;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.example.ideo7.weather.activity.MyApplication;
import com.facebook.stetho.Stetho;


public class DebugApplication extends MyApplication {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        XLog.init(LogLevel.ALL);
    }
}