package com.stories.sunny.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.stories.sunny.R;
import com.stories.sunny.gson_model.HourlyForecast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Charlottecao on 9/19/17.
 */

public class LineCharView extends View {

    /**
     * Custom preferences
     */
    private int mTimeLineInterval; //时间线段长度
    private int mBackgroundColor; //背景颜色
    private int mMinPointHeight; //折线最低点的高度

    private int mMinViewHeight; //控件的最小高度
    private float mPointRadius; //折线点的半径
    private float mTextSize; //字体大小
    private float mPointGap; //折线单位高度差
    private int mDefaultPadding; //折线坐标图四周留出来的偏移量
    private float mIconWidth;  //天气图标的边长

    private int mViewHeight;
    private int mViewWidth;

    private int mScreenWidth;
    private int mScreenHeight;

    /**
     * Paint
     */
    private Paint mLinePaint; //线画笔
    private Paint mTextPaint; //文字画笔
    private Paint mPointPaint; //圆点画笔

    private List<HourlyForecast> mHourlyForecastList = new ArrayList<>();

    public void setData(List<HourlyForecast> hourlyForecastList) {
        if (hourlyForecastList == null || hourlyForecastList.isEmpty()) {
            return;
        }
        mHourlyForecastList = hourlyForecastList;

        requestLayout();
        invalidate();
    }

    public LineCharView(Context context) {
        this(context, null);
    }

    public LineCharView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineCharView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineCharView);
        mTimeLineInterval = (int) typedArray.getDimension(R.styleable.LineCharView_timeLineInterval, dp2pxF(context, 60));
        mBackgroundColor = typedArray.getColor(R.styleable.LineCharView_backgroundColor, Color.WHITE);
        mMinPointHeight = (int) typedArray.getDimension(R.styleable.LineCharView_minPointHeight, dp2pxF(context, 60));
        typedArray.recycle();

        initDefaultPreferences(context);

        initPaint(context);
    }

    private void initDefaultPreferences(Context context) {
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        mMinViewHeight = 3 * mMinPointHeight;
        mPointRadius = dp2pxF(context, 2.5F);
        mTextSize = sp2px(context, 10);
        mDefaultPadding = (int) (0.5 * mMinPointHeight);  //默认0.5倍
        mIconWidth = (1.0f / 3.0f) * mTimeLineInterval; //默认1/3倍
    }

    private void initPaint(Context context) {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(dp2px(context, 1));

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStrokeWidth(dp2px(context, 1));
    }


    /**
     *Utils
     */
    public static int dp2px(Context c, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context c, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.getResources().getDisplayMetrics());
    }

    public static float dp2pxF(Context c, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, c.getResources().getDisplayMetrics());
    }

    public static float sp2pxF(Context c, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, c.getResources().getDisplayMetrics());
    }
}
