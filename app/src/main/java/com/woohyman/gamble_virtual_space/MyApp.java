package com.woohyman.Gamble_virtual_space;

import android.app.Application;

import com.woohyman.Gamble_virtual_space.manager.FloatBallManager;
import com.woohyman.Gamble_virtual_space.manager.heartBeat;

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
