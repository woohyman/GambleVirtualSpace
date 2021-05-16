package com.example.ui_exmaple;

import android.app.Application;

import com.example.ui_exmaple.manager.heartBeat;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        heartBeat.getInstance().init();
    }
}
