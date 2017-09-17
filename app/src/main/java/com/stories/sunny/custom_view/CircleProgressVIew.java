package com.stories.sunny.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.stories.sunny.R;

/**
 * Created by Charlottecao on 9/16/17.
 */

public class CircleProgressVIew extends View {

    /**
     * Custom preference
     */
    private float mRadius;
    private float mProgressBarWidth;
    private int mPogressBarColor;
    private int mTextColor;
    private int mMaxProgress;


    private Paint mBackgroundPaint;
    private Paint mBigCirclePaint;
    private Paint mArcPaint;
    private Paint mTextPaint;

    private int mCircleCenterX;
    private int mCircleCenterY;

    private float mTextHeight;
    private float mTextWidth;

    private int mProgress = 30;

    private RectF mRectF;

    public CircleProgressVIew(Context context) {
        this(context, null);
    }

    public CircleProgressVIew(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressVIew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /**
         * Get Custom preference
         */
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, 0, 0);
        mRadius = typedArray.getDimension(R.styleable.CircleProgressView_radius, 100);
        mProgressBarWidth = typedArray.getDimension(R.styleable.CircleProgressView_progress_bar_width, 20);
        mPogressBarColor = typedArray.getColor(R.styleable.CircleProgressView_progress_bar_color, Color.BLACK);
        mTextColor = typedArray.getColor(R.styleable.CircleProgressView_text_color, Color.BLACK);
        mMaxProgress = typedArray.getInt(R.styleable.CircleProgressView_max_progress, 100);
        typedArray.recycle();

        init();
    }


    /**
     * Initialize Paint
     */
    private void init() {
        mRectF = new RectF();

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(Color.BLUE);
        mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mProgressBarWidth);
        mArcPaint.setColor(mPogressBarColor);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mRadius / 2);

        mBigCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.YELLOW);
        mBigCirclePaint.setStyle(Paint.Style.FILL);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = (int) Math.ceil(fm.descent - fm.ascent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCircleCenterX = getWidth() / 2;
        mCircleCenterY = getHeight() / 2;

        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mRadius + mProgressBarWidth, mBigCirclePaint);
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mRadius, mBackgroundPaint);

        if (mMaxProgress > 0) {
            RectF oval = new RectF();
            oval.left = (mCircleCenterX - mProgressBarWidth);
            oval.top = (mCircleCenterY - mProgressBarWidth);
            oval.right = mProgressBarWidth * 2 + (mCircleCenterX - mProgressBarWidth);
            oval.bottom = mProgressBarWidth * 2 + (mCircleCenterY - mProgressBarWidth);
            canvas.drawArc(oval, -90, ((float) mProgress / mMaxProgress) * 360, false, mArcPaint);

            String txt = "优";
            mTextWidth =  mTextPaint.measureText(txt, 0, txt.length());
            canvas.drawText(txt, mCircleCenterX - mTextWidth / 2, mCircleCenterY + mTextHeight / 4, mTextPaint);
        }
    }

    public void setProgress(int progress) {
        mProgress = progress;
        postInvalidate();
    }

    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }
}
