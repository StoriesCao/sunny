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

public class CircleProgressView extends View {

    /**
     * Custom preference
     */
    private float mRadius;
    private float mProgressBarWidth;
    private int mProgressBarColor;
    private int mInnerCircleColor;
    private int mOuterCircleColor;
    private int mTextColor;
    private float mTextSize;
    private int mMaxProgress;

    /**
     * CircleProgressView default size
     */
    private int mDefaultWidth = 200;
    private int mDefaultHeight = 200;

    /**
     * Paint
     */
    private Paint mOuterCirclePaint;
    private Paint mInnerCirclePaint;
    private Paint mArcPaint;
    private Paint mArcFillPaint;
    private Paint mTextPaint;

    private int mCircleCenterX;
    private int mCircleCenterY;

    private String mTextString = "优";

    private float mTextHeight;
    private float mTextWidth;

    private int mCurrentProgress = 60;

    /**
     * Constructor
     */
    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        /**
         * Get Custom preference
         */
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, 0, 0);
        mRadius = typedArray.getDimension(R.styleable.CircleProgressView_radius, 80);
        mProgressBarWidth = typedArray.getDimension(R.styleable.CircleProgressView_progressBarWidth, 20);
        mProgressBarColor = typedArray.getColor(R.styleable.CircleProgressView_progressBarColor, Color.BLACK);
        mInnerCircleColor = typedArray.getColor(R.styleable.CircleProgressView_innerCircleColor, Color.WHITE);
        mOuterCircleColor = typedArray.getColor(R.styleable.CircleProgressView_outerCircleColor, Color.WHITE);
        mTextColor = typedArray.getColor(R.styleable.CircleProgressView_textColor, Color.BLACK);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.CircleProgressView_textSize, 30);
        mMaxProgress = typedArray.getInt(R.styleable.CircleProgressView_maxProgress, 100);
        typedArray.recycle();

        init();
    }


    /**
     * Initialize Paint
     */
    private void init() {
        mOuterCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mOuterCirclePaint.setStyle(Paint.Style.FILL);
        mOuterCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterCirclePaint.setColor(mOuterCircleColor);

        mInnerCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInnerCirclePaint.setStyle(Paint.Style.FILL);
        mInnerCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerCirclePaint.setColor(mInnerCircleColor);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mProgressBarWidth);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcPaint.setColor(mProgressBarColor);

        mArcFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcFillPaint.setStrokeCap(Paint.Cap.ROUND);
        mArcFillPaint.setColor(Color.GRAY);
        mArcFillPaint.setStrokeWidth(mProgressBarWidth);
        mArcFillPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTextHeight = (int) Math.ceil(fm.descent - fm.ascent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, mDefaultHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mDefaultWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mDefaultHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCircleCenterX = getWidth() / 2;
        mCircleCenterY = getHeight() / 2;

        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mRadius + mProgressBarWidth, mOuterCirclePaint);
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mRadius, mInnerCirclePaint);

        if (mMaxProgress > 0) {
            RectF oval = new RectF();
            oval.left = (mCircleCenterX - mRadius - mProgressBarWidth / 2);
            oval.top = (mCircleCenterY - mRadius - mProgressBarWidth / 2);
            oval.right = mCircleCenterX + mRadius + mProgressBarWidth / 2;
            oval.bottom = mCircleCenterY + mRadius + mProgressBarWidth / 2;
            canvas.drawArc(oval, -180, 360, false, mArcFillPaint);
            canvas.drawArc(oval, -180, ((float) mCurrentProgress / mMaxProgress) * 360, false, mArcPaint);

            if (!mTextString.isEmpty()) {
                mTextWidth =  mTextPaint.measureText(mTextString, 0, mTextString.length());
                canvas.drawText(mTextString, mCircleCenterX - mTextWidth / 2, mCircleCenterY + mTextHeight / 4, mTextPaint);
            }

        }
    }

    public void setProgress(int progress) {
        mCurrentProgress = progress;
        postInvalidate();
    }

    public void setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
    }

    public void setTextString(String textString) {
        mTextString = textString;
    }
}
