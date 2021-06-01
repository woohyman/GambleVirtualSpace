package com.woohyman.swipe_assistant;

import android.app.Application;

import com.woohyman.swipe_assistant.manager.FloatBallManager;
import com.woohyman.swipe_assistant.manager.heartBeat;

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
