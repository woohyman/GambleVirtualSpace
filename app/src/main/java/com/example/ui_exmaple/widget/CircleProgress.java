package com.example.ui_exmaple.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.Keep;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.ui_exmaple.R;

import timber.log.Timber;

public class CircleProgress extends View {

    public static final int PROGRESS_INTERVAL = 1000;

    public static final int DEFAULT_PROGRESS = 0;
    public static final int DEFAULT_DURATION = 2000;

    public static final int DEFAULT_PROGRESS_WIDTH = ConvertUtils.dp2px(2);
    public static final int DEFAULT_PROGRESS_BG_COLOR = Color.BLACK;
    public static final int DEFAULT_PROGRESS_COLOR = Color.RED;

    private Paint mBgPaint;
    private Paint mProgressPaint;
    private RectF mRectF;

    private float mStartAngle = -90;
    private float mProgressWidth;
    private int mProgress = 0;

    private long mMoment;
    private long mTime;
    private Runnable mAutoRunnable;

    private OnCircleProgressListener listener;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);

        mProgressWidth = mTypedArray.getDimensionPixelOffset(R.styleable.CircleProgress_progress_width, DEFAULT_PROGRESS_WIDTH);
        int progressBgColor = mTypedArray.getColor(R.styleable.CircleProgress_progress_bg_color, DEFAULT_PROGRESS_BG_COLOR);
        int progressColor = mTypedArray.getColor(R.styleable.CircleProgress_progress_color, DEFAULT_PROGRESS_COLOR);
        mProgress = mTypedArray.getInteger(R.styleable.CircleProgress_progress, DEFAULT_PROGRESS);
        int locationStart = mTypedArray.getInt(R.styleable.CircleProgress_location_start, 2);
        mTypedArray.recycle();

        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(mProgressWidth);
        mBgPaint.setColor(progressBgColor);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressWidth);
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        switch (locationStart) {
            case 1:
                mStartAngle = -180;
                break;
            case 2:
                mStartAngle = -90;
                break;
            case 3:
                mStartAngle = 0;
                break;
            case 4:
                mStartAngle = 90;
                break;
        }

        mAutoRunnable = new AutoRunnable();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(mProgressWidth / 2, mProgressWidth / 2, getWidth() - mProgressWidth / 2, getHeight() - mProgressWidth / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(widthSize, heightSize);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float cx = getWidth() / 2F;
        float cy = getHeight() / 2F;
        float radius = getWidth() / 2F - mProgressWidth / 2F;
        canvas.drawCircle(cx, cy, radius, mBgPaint);

        float sweepAngle = 360 / 100F * mProgress;
        canvas.drawArc(mRectF, mStartAngle, sweepAngle, false, mProgressPaint);
    }

    public void start(Long moment) {
        this.mMoment = moment;
        if (moment > 0) {
            removeCallbacks(mAutoRunnable);
            autoProgressInterval(mProgress);
        }
        Timber.tag("CircleProgress").e("start  ");
    }

    public void stop() {
        removeCallbacks(mAutoRunnable);
        Timber.tag("CircleProgress").e("stop  ");
    }

    public boolean isDone() {
        return mProgress >= 100;
    }

    public void reset() {
        mProgress = 0;
        mTime = 0;
        setProgress(mProgress);
    }

    private void autoProgressInterval(int progress) {
        setProgress(progress);
        postDelayed(mAutoRunnable, PROGRESS_INTERVAL);
    }

    public void startAnimProgress(@IntRange(from = 0, to = 100) int endProgress) {
        startAnimProgress(endProgress, DEFAULT_DURATION);
    }

    public void startAnimProgress(int endProgress, int duration) {
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "progress", 0, endProgress);
        animator.setDuration(duration);
        animator.addUpdateListener(animation -> {
            int progress = (int) animation.getAnimatedValue();
            setProgress(progress);
        });
        animator.start();
    }

    @Keep
    public int getProgress() {
        return mProgress;
    }

    @Keep
    private void setProgress(@IntRange(from = 0, to = 100) int progress) {
        this.mProgress = progress;
        invalidate();
        if (listener != null) {
            listener.onProgressChange(progress);
        }
    }

    public void setOnCircleProgressListener(OnCircleProgressListener listener) {
        this.listener = listener;
    }

    public interface OnCircleProgressListener {
        void onProgressChange(int progress);
    }

    public class AutoRunnable implements Runnable {

        @Override
        public void run() {
            mTime = mTime + PROGRESS_INTERVAL;
            mProgress = (int) (mTime * 100f / mMoment);
            if (mProgress <= 100) {
                autoProgressInterval(mProgress);
            } else {
                mProgress = 100;
            }
            //Timber.tag("CircleProgress").e("run  " + mTime + " " + mMoment + " " + mProgress);
        }
    }
}
