package com.woohyman.Gamble_virtual_space.widget;

import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.TouchUtils;
import com.blankj.utilcode.util.Utils;
import com.woohyman.Gamble_virtual_space.R;
import com.woohyman.Gamble_virtual_space.manager.FloatBallManager;

public class FloatBall extends RelativeLayout {

    private static final FloatBall INSTANCE = new FloatBall();

    private final CircleProgress mCircleProgress;
    private final LottieAnimationView mLottieAnimationView;
    private final ImageView mFloatBallStatic;
    private final TextView mTvRewardCount;
    private CircleProgress.OnCircleProgressListener listener;

    public static FloatBall getInstance() {
        return INSTANCE;
    }

    public static void setVisibility(boolean isShow) {
        if (INSTANCE == null) {
            return;
        }
        INSTANCE.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void setOnCircleProgressListener(CircleProgress.OnCircleProgressListener listener) {
        this.listener = listener;
    }

    public static final String TASK_FLOAT_BALL_ANIMATION_JSON = "float_ball/data.json";
    public static final String TASK_FLOAT_BALL_ANIMATION_IMAGES = "float_ball/images/";

    public FloatBall() {
        super(Utils.getApp());
        inflate(Utils.getApp(), R.layout.layout_float_ball, this);

        mTvRewardCount = findViewById(R.id.tv_reward_count);

        mLottieAnimationView = findViewById(R.id.float_bal_lottie);
        mLottieAnimationView.setAnimation(TASK_FLOAT_BALL_ANIMATION_JSON);
        mLottieAnimationView.setImageAssetsFolder(TASK_FLOAT_BALL_ANIMATION_IMAGES);
        mLottieAnimationView.setClickable(false);

        mFloatBallStatic = findViewById(R.id.iv_float_ball_static);
        mCircleProgress = findViewById(R.id.circleProgress);
        mCircleProgress.setOnCircleProgressListener(progress -> {
            listener.onProgressChange(progress);
            showRewardAnim(progress >= 100);
        });

        TouchUtils.setOnTouchListener(this, new TouchUtils.OnTouchUtilsListener() {

            private int rootViewWidth;
            private int rootViewHeight;
            private int viewWidth;
            private int viewHeight;
            private int statusBarHeight;

            @Override
            public boolean onDown(View view, int x, int y, MotionEvent event) {
                viewWidth = view.getWidth();
                viewHeight = view.getHeight();
                View contentView = view.getRootView().findViewById(android.R.id.content);
                rootViewWidth = contentView.getWidth();
                rootViewHeight = contentView.getHeight();
                statusBarHeight = BarUtils.getStatusBarHeight();

                processScale(view, true);
                return true;
            }

            @Override
            public boolean onMove(View view, int direction, int x, int y, int dx, int dy, int totalX, int totalY, MotionEvent event) {
                view.setX(Math.min(Math.max(0, view.getX() + dx), rootViewWidth - viewWidth));
                view.setY(Math.min(Math.max(statusBarHeight, view.getY() + dy), rootViewHeight - viewHeight));
                return true;
            }

            @Override
            public boolean onStop(View view, int direction, int x, int y, int totalX, int totalY, int vx, int vy, MotionEvent event) {
                stick2HorizontalSide(view);
                processScale(view, false);
                return true;
            }

            private void stick2HorizontalSide(View view) {
                view.animate()
                        .setInterpolator(new DecelerateInterpolator())
                        .translationX(view.getX() + viewWidth / 2f > rootViewWidth / 2f ? rootViewWidth - viewWidth : 0)
                        .setDuration(100)
                        .withEndAction(() -> savePosition())
                        .start();
            }

            private void processScale(final View view, boolean isDown) {
                float value = isDown ? 1 - 0.1f : 1;
                view.animate()
                        .scaleX(value)
                        .scaleY(value)
                        .setDuration(100)
                        .start();
            }
        });

        ClickUtils.applyGlobalDebouncing(this, v -> FloatBallManager.get().onFloatBallClicked());
    }

    public void showRewardAnim(boolean canReward) {
        if (canReward) {
            if (!mLottieAnimationView.isAnimating()) {
                mLottieAnimationView.playAnimation();
                mLottieAnimationView.setVisibility(VISIBLE);
                mFloatBallStatic.setVisibility(INVISIBLE);
            }
        } else {
            if (mLottieAnimationView.isAnimating()) {
                mLottieAnimationView.pauseAnimation();
                mLottieAnimationView.setVisibility(INVISIBLE);
                mFloatBallStatic.setVisibility(VISIBLE);
            }
        }
    }

    public void updateRewardCount(String rewardCoin) {
        mTvRewardCount.setText(rewardCoin);
    }

    public void reset() {
        mCircleProgress.reset();
    }

    public void start(Long moment) {
        mCircleProgress.start(moment);
    }

    public void stop() {
        mCircleProgress.stop();
    }

    public boolean isDone() {
        return mCircleProgress.isDone();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        wrapPosition();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        savePosition();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        wrapPosition();
    }

    private void savePosition() {
        FloatBallConfig.saveViewX(this, (int) getX());
        FloatBallConfig.saveViewY(this, (int) getY());
    }

    private void wrapPosition() {
        post(() -> {
            View contentView = getRootView().findViewById(android.R.id.content);
            if (contentView == null) {
                return;
            }
            setX(FloatBallConfig.getViewX(FloatBall.this));
            setY(FloatBallConfig.getViewY(FloatBall.this, contentView.getHeight() / 3));
            setX(getX() + getWidth() / 2f > contentView.getWidth() / 2f ? contentView.getWidth() - getWidth() : 0);
        });
    }

    private static class FloatBallConfig {

        public static void saveViewY(View view, int y) {
            if (ScreenUtils.isPortrait()) {
                getSp().put(view.getClass().getSimpleName() + ".yP", y);
            } else {
                getSp().put(view.getClass().getSimpleName() + ".yL", y);
            }
        }

        public static int getViewY(View view) {
            return getViewY(view, 0);
        }

        public static int getViewY(View view, int defaultVal) {
            if (ScreenUtils.isPortrait()) {
                return getSp().getInt(view.getClass().getSimpleName() + ".yP", defaultVal);
            } else {
                return getSp().getInt(view.getClass().getSimpleName() + ".yL", defaultVal);
            }
        }

        public static void saveViewX(View view, int x) {
            if (ScreenUtils.isPortrait()) {
                getSp().put(view.getClass().getSimpleName() + ".xP", x);
            } else {
                getSp().put(view.getClass().getSimpleName() + ".xL", x);
            }
        }

        public static int getViewX(View view) {
            if (ScreenUtils.isPortrait()) {
                return getSp().getInt(view.getClass().getSimpleName() + ".xP");
            } else {
                return getSp().getInt(view.getClass().getSimpleName() + ".xL");
            }
        }

        public static void saveViewHeight(View view, int height) {
            if (ScreenUtils.isPortrait()) {
                getSp().put(view.getClass().getSimpleName() + ".heightP", height);
            } else {
                getSp().put(view.getClass().getSimpleName() + ".heightL", height);
            }
        }

        public static int getViewHeight(View view, int height) {
            if (ScreenUtils.isPortrait()) {
                return getSp().getInt(view.getClass().getSimpleName() + ".heightP", height);
            } else {
                return getSp().getInt(view.getClass().getSimpleName() + ".heightL", height);
            }
        }

        public static void saveViewAlpha(View view, float alpha) {
            getSp().put(view.getClass().getSimpleName() + ".alpha", alpha);
        }

        public static float getViewAlpha(View view) {
            return getSp().getFloat(view.getClass().getSimpleName() + ".alpha", 1f);
        }

        private static SPUtils getSp() {
            return SPUtils.getInstance("FloatBall");
        }
    }
}
