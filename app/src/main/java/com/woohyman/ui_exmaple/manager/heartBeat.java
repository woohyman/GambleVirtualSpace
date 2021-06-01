package com.woohyman.swipe_assistant.manager;

import android.util.Pair;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

public class heartBeat {
    private int progress;

    private heartBeat() {

    }

    public void init(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                progress = progress<=100?++progress:0;
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask,0,1000);
    }

    public static heartBeat getInstance() {
        return inner.instance;
    }

    public int getProgress() {
        return progress;
    }

    public static class inner {
        private static final heartBeat instance = new heartBeat();
    }
}
