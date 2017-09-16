package com.stories.sunny.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Charlottecao on 9/16/17.
 */

public class SemicircleVIew extends View {

    private Paint mBackgroundPaint;
    private Paint mArcPaint;

    private Float mRadius;

    private RectF mRectF;

    public SemicircleVIew(Context context) {
        this(context, null);
    }

    public SemicircleVIew(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SemicircleVIew(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
         mRectF = new RectF();

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(0xFF343434);

        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(4F);
        mArcPaint.setColor(0x000000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mRectF, -180, 0, false, mBackgroundPaint);
    }
}
