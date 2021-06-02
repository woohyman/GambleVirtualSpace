package com.woohyman.Gamble_virtual_space.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.woohyman.Gamble_virtual_space.widget.FloatBall;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FloatBallManager extends Utils.ActivityLifecycleCallbacks {

    private static final FloatBallManager INSTANCE = new FloatBallManager();
    private static final long MINUTE = 1000 * 6;
    private int mStep = 0;

    private final List<Integer> mStepCompleteNode = Arrays.asList(1, 6, 9, 15, 20, 30);
    private final List<Integer> mStepTimeInterval = new ArrayList<>();
    private final List<Long> mStepMoment = new ArrayList<>();

    private final ViewGroup.LayoutParams mParams = new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
    );

    private int mProgress;

    public static FloatBallManager get() {
        return INSTANCE;
    }

    private FloatBallManager() {
        for (int i = 0; i < mStepCompleteNode.size(); i++) {
            if (i == 0) {
                mStepTimeInterval.add(mStepCompleteNode.get(0));
            } else {
                mStepTimeInterval.add(mStepCompleteNode.get(i) - mStepCompleteNode.get(i - 1));
            }
        }
        for (int item : mStepTimeInterval) {
            mStepMoment.add(item * MINUTE);
        }
    }

    public void init() {
        ActivityUtils.addActivityLifecycleCallbacks(this);
        FloatBall.getInstance().setOnCircleProgressListener(progress -> {
            mProgress = progress;
        });
    }

    public void onFloatBallClicked() {
        if (mProgress < 100) {
            ToastUtils.showLong("请等待完成后点击！！！");
            return;
        }

        if (mStep >= mStepMoment.size()) {
            mStep = 0;
        } else {
            mStep++;
        }
        startFloatBallCycle(true);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        FloatBall floatBall = FloatBall.getInstance();
        ViewParent parent = floatBall.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(FloatBall.getInstance());
        }

        ((ViewGroup) activity.findViewById(android.R.id.content)).addView(floatBall, mParams);

        startFloatBallCycle(false);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        FloatBall floatBall = FloatBall.getInstance();
        ((ViewGroup) activity.findViewById(android.R.id.content)).removeView(floatBall);

        floatBall.stop();
    }

    int progress = 1;

    private void startFloatBallCycle(boolean resetBeforeStart) {
        FloatBall floatBall = FloatBall.getInstance();

        if (progress < mStepMoment.size()) {
            if (resetBeforeStart) {
                floatBall.reset();
            }
            floatBall.start(mStepMoment.get(progress));
        } else {
            floatBall.reset();
            floatBall.stop();
        }
    }

    public List<Integer> getStepCompleteNode() {
        return mStepCompleteNode;
    }

    public int getStepSize() {
        return mStepCompleteNode.size();
    }

    public int getTotalCount() {
        return mStepCompleteNode.get(mStepCompleteNode.size() - 1);
    }

    public List<Integer> getStepTimeInterval() {
        return mStepTimeInterval;
    }

    public int getProgress() {
        return mProgress;
    }

    public int getStep() {
        return mStep;
    }
}
