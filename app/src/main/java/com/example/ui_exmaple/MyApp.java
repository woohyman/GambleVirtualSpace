package com.example.ui_exmaple;

import android.app.Application;

import com.example.ui_exmaple.manager.FloatBallManager;
import com.example.ui_exmaple.manager.heartBeat;

import timber.log.Timber;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        heartBeat.getInstance().init();

        Timber.plant(new Timber.DebugTree());
        FloatBallManager.get().init();
    }
}
