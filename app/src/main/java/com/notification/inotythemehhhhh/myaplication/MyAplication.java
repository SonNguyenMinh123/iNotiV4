package com.notification.inotythemehhhhh.myaplication;

import android.app.Application;
import android.support.multidex.MultiDex;

public class MyAplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
    }
}
