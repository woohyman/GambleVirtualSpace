package com.example.ui_exmaple.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class SegmentProgressBar extends View {
    /**
     * 设置各种默认值
     */
    private static final int DEFAULT_HEIGHT_PROGRESS_BAR = 10;
    /**
     * 进度条圆角
     */
    private static final float mRadius = 0;
    /**
     * 背景色
     */
    private int defaultBackgroundColor = Color.parseColor("#DDE4F4");
    /**
     * 进度条颜色
     */
    private int defaultProgressBarColor = Color.parseColor("#3D7EFE");

    /**
     * 所有画图所用的画笔
     */
    protected Paint mPaint = new Paint();
    /**
     * 进度条间距
     */
    protected float mOffset = 0;
    protected float mDefaultOffset = 10;

    /**
     * 进度条高度
     */
    protected int mProgressBarHeight = dp2px(DEFAULT_HEIGHT_PROGRESS_BAR);

    /**
     * 除padding外的视图宽度
     */
    protected float mRealWidth;
    /**
     * 最大值
     */
    private int mMax = 100;
    /**
     * 当前进度
     */
    private int mProgress = 0;
    /**
     * 分段宽度
     */
    private float progressWith = 0;


    public SegmentProgressBar(Context context) {
        this(context, null);
    }

    public SegmentProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SegmentProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化布局
     */
    private void init() {
    }


    /**
     * 最大值
     *
     * @param max
     */
    public void setMax(int max) {
        this.mMax = max;
        if (max > 0) {
            mOffset = mRealWidth / mMax / 8;
            if (mOffset > mDefaultOffset) {
                mOffset = mDefaultOffset;
            }
            progressWith = (mRealWidth - (mMax - 1) * mOffset) / mMax;
        }
        invalidate();
    }

    /**
     * 进度值
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec)*2;
        //高度
        int height = measureHeight(heightMeasureSpec);
        //必须调用该方法来存储View经过测量的到的宽度和高度
        setMeasuredDimension(width, height);
        //真正的宽度值是减去左右padding
        mRealWidth = getMeasuredWidth() - getPaddingRight() - getPaddingLeft();
        //使用画笔在画布上绘制进度
        if (mMax > 0) {
            mOffset = mRealWidth / mMax / 8;
            if (mOffset > mDefaultOffset) {
                mOffset = mDefaultOffset;
            }
            progressWith = (mRealWidth - (mMax - 1) * mOffset) / mMax;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //真正的宽度值是减去左右padding
        mRealWidth = w - getPaddingRight() - getPaddingLeft();
        //使用画笔在画布上绘制进度
        if (mMax > 0) {
            mOffset = mRealWidth / mMax / 8;
            if (mOffset > mDefaultOffset) {
                mOffset = mDefaultOffset;
            }
            progressWith = (mRealWidth - (mMax - 1) * mOffset) / mMax;
        }
        invalidate();
    }

    /**
     * EXACTLY：父控件告诉我们子控件了一个确定的大小，你就按这个大小来布局。比如我们指定了确定的dp值和macth_parent的情况。
     * AT_MOST：当前控件不能超过一个固定的最大值，一般是wrap_content的情况。
     * UNSPECIFIED:当前控件没有限制，要多大就有多大，这种情况很少出现。
     *
     * @param measureSpec
     * @return 视图的高度
     */
    private int measureHeight(int measureSpec) {
        int result = 0;
        //父布局告诉我们控件的类型
        int specMode = MeasureSpec.getMode(measureSpec);
        //父布局传过来的视图大小
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (getPaddingTop() + getPaddingBottom() + mProgressBarHeight);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        SegmentByXfermode(canvas);
    }


    private void SegmentByXfermode(Canvas canvas){
        int sc = canvas.saveLayer(0, 0, mRealWidth, getHeight(), null, Canvas.ALL_SAVE_FLAG);

        mPaint.setColor(defaultBackgroundColor);
        //设置画笔类型
        mPaint.setStyle(Paint.Style.FILL);
        //去除锯齿
        mPaint.setAntiAlias(true);
        //使用画笔在画布上绘制背景
        canvas.drawRoundRect(0, 0, mRealWidth, getHeight(), mRadius, mRadius, mPaint);
        //绘制进度条
        mPaint.setColor(defaultProgressBarColor);
        canvas.drawRoundRect(0, 0, mRealWidth/100*mProgress, getHeight(), mRadius, mRadius, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        canvas.drawRoundRect(mRealWidth/8, 0, mRealWidth/8+10, getHeight(), mRadius, mRadius, mPaint);

        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);
    }

    private void SegmentByArray(Canvas canvas) {

    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
