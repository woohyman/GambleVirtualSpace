package com.woohyman.gameble_virtual_space.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.woohyman.gameble_virtual_space.R;
import com.woohyman.gameble_virtual_space.manager.FloatBallManager;

import java.util.ArrayList;
import java.util.List;

public class SegmentProgressBar extends View {
    //进度条圆角
    private static final float mRadius = 0;

    //不同线段之间的间隔
    private final int mInterval = ConvertUtils.dp2px(6);
    private final int mMax = 100;
    private final Paint mPaint = new Paint();
    private final int mRightDistance = ConvertUtils.dp2px(50);
    private final List<Float> mStarDrawTranslationX = new ArrayList<>();
    private final List<Float> mEndDrawTranslationX = new ArrayList<>();
    private final List<Float> mSegmentWidth = new ArrayList<>();
    private final List<Float> mTipTranslationX = new ArrayList<>();

    protected float mRealWidth;
    private int mProgress = 0;
    private int mStep = 0;

    private Paint mTextPaint;

    public SegmentProgressBar(Context context) {
        this(context, null);
    }

    public SegmentProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SegmentProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (wSpecMode == MeasureSpec.AT_MOST && hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, 300);
        } else if (wSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(300, hSpecSize);
        } else if (hSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(wSpecSize * 2, 300);
        }

        init(getMeasuredWidth());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init(w);
        invalidate();
    }

    private Runnable mAutoRunnable;

    public void start() {
        if (mAutoRunnable == null) {
            mAutoRunnable = new Runnable() {
                @Override
                public void run() {
                    invalidate();
                    postDelayed(this, 1000);
                }
            };
        }
        removeCallbacks(mAutoRunnable);
        post(mAutoRunnable);
    }

    public void stop() {
        removeCallbacks(mAutoRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        mProgress = FloatBallManager.get().getProgress();
        mStep = FloatBallManager.get().getStep();

        for (int i = 0, size = FloatBallManager.get().getStepSize(); i < size; i++) {
            //绘制前景
            if (i < mStep) {
                mPaint.setColor(ColorUtils.getColor(R.color.online_progress_normal));
                canvas.drawRoundRect(mStarDrawTranslationX.get(i), getHeight() - 20, mEndDrawTranslationX.get(i), getHeight(), mRadius, mRadius, mPaint);
            }
            //绘制背景
            else {
                mPaint.setColor(ColorUtils.getColor(R.color.white));
                canvas.drawRoundRect(mStarDrawTranslationX.get(i), getHeight() - 20, mEndDrawTranslationX.get(i), getHeight(), mRadius, mRadius, mPaint);
            }
            //绘制正在增加的进度
            if (i == mStep) {
                mPaint.setColor(ColorUtils.getColor(R.color.online_progress_normal));
                canvas.drawRoundRect(mStarDrawTranslationX.get(mStep), getHeight() - 20, mStarDrawTranslationX.get(mStep) + mSegmentWidth.get(mStep) / mMax * mProgress, getHeight(), mRadius, mRadius, mPaint);
            }
        }

        String tip = "progress=" + mProgress;
        //拿到字符串的宽度
        float stringWidth = mTextPaint.measureText(tip);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();

        Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        mTextPaint.setTypeface(font);

        mTextPaint.setStyle(Paint.Style.STROKE);

        float offsetX = mStarDrawTranslationX.get(mStep) + mSegmentWidth.get(mStep) / mMax * mProgress - stringWidth / 2;
        canvas.drawText(tip, offsetX, getHeight() - 20 - fontMetrics.descent, mTextPaint);
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    private void init(int width) {
        mRealWidth = width - getPaddingRight() - getPaddingLeft() - mInterval * (FloatBallManager.get().getStepSize() - 1) - mRightDistance;
        float widthUnit = mRealWidth / FloatBallManager.get().getTotalCount();
        List<Integer> stepCompleteNode = FloatBallManager.get().getStepCompleteNode();
        List<Integer> stepTimeInterval = FloatBallManager.get().getStepTimeInterval();
        for (int i = 0; i < FloatBallManager.get().getStepSize(); i++) {
            mStarDrawTranslationX.add(i == 0 ? 0 : widthUnit * stepCompleteNode.get(i - 1) + mInterval * i);
            mEndDrawTranslationX.add(widthUnit * stepCompleteNode.get(i) + mInterval * i);
            mSegmentWidth.add(stepTimeInterval.get(i) * widthUnit);
            mTipTranslationX.add(widthUnit * stepCompleteNode.get(i) + mInterval * (1 / 2f + i));
        }

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#FF4081"));
        mTextPaint.setTextSize(30f);
    }

    public int getRightDistance() {
        return mRightDistance;
    }

    public List<Float> getTipLoc() {
        return mTipTranslationX;
    }
}
