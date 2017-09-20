package com.stories.sunny.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.stories.sunny.R;
import com.stories.sunny.gson_model.HourlyForecast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    private int mMaxTemperature;  //元数据中的最高和最低温度
    private int mMinTemperature;

    /**
     * Paint
     */
    private Paint mLinePaint; //线画笔
    private Paint mTextPaint; //文字画笔
    private Paint mPointPaint; //圆点画笔

    String[] mWeatherConditionsString = {"晴", "阴", "风", "小雨", "雷阵雨", "大雨", "小雪", "中雪", "大雪", "雾", "未知"};

    private List<HourlyForecast> mHourlyForecastList = new ArrayList<>(); // JSON数据集合
    private Map<String, Bitmap> mIcons = new HashMap<>();   //天气图标集合
    private List<Pair<Integer, String>> mSameWeatherGroupData = new ArrayList<>();  //对元数据中天气分组后的集合
    private List<Float> mDifferentWeatherXData = new ArrayList<>();  //不同天气之间虚线的x坐标集合
    private List<PointF> mPoints = new ArrayList<>(); //折线拐点坐标集合


    /**
     *
     * @param hourlyForecastList
     */
    public void setData(List<HourlyForecast> hourlyForecastList) {
        if (hourlyForecastList == null || hourlyForecastList.isEmpty()) {
            return;
        }
        mHourlyForecastList = hourlyForecastList;
        mSameWeatherGroupData.clear();
        mDifferentWeatherXData.clear();
        mPoints.clear();

        initSameWeatherGroup();
        requestLayout();
        invalidate();
    }


    /**
     * Constructor
     * @param context
     */
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

        setBackgroundColor(mBackgroundColor);

        initDefaultPreferences(context);

        initPaint(context);

        initIcon();
    }

    /**
     * Initialize
     */

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

    private void initIcon() {
        mIcons.clear();
        for (int i = 0; i < mWeatherConditionsString.length; i++) {
            Bitmap bitmap = getIconBitmap(mWeatherConditionsString[i], mIconWidth, mIconWidth);
            mIcons.put(mWeatherConditionsString[i], bitmap);
        }

    }

    /**
     *
     * @param weatherString
     * @param requestW
     * @param requestH
     * @return
     */
    private Bitmap getIconBitmap(String weatherString, float requestW, float requestH) {
        int resId = getIconResId(weatherString);
        Bitmap bitmap;
        int outWidth, outHeight;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
        outHeight = options.outHeight;
        outWidth = options.outWidth;
        options.inSampleSize = 1;
        if (outWidth > requestW || outHeight > requestH) {
            int ratioW = Math.round(outWidth / requestW);
            int ratioH = Math.round(outHeight / requestH);
            options.inSampleSize = Math.max(ratioW, ratioH);  //压缩
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeResource(getResources(), resId,options);
        return bitmap;
    }

    /**
     * Get weather icon
     *
     * @param weatherString
     * @return
     */
    private int getIconResId(String weatherString) {
        int resId;

        if (weatherString.equals("晴")) { // 晴
            resId = R.drawable.ic_sunny;
        } else if (weatherString.equals("阴")) { //阴
            resId = R.drawable.ic_cloud;
        } else if (weatherString.equals("风")) { //风
            resId = R.drawable.ic_wind;
        } else if (weatherString.equals("小雨")) { //阵雨
            resId = R.drawable.ic_light_rain;
        } else if (weatherString .equals("雷阵雨")) { //雷阵雨
            resId = R.drawable.ic_thunder;
        } else if (weatherString.equals("大雨")) { //大（暴）雨
            resId = R.drawable.ic_heavy_rain;
        } else if (weatherString.equals("小雪")) { //小雪 阵雪
            resId = R.drawable.ic_light_snow;
        } else if (weatherString.equals("中雪")) { //中雪
            resId = R.drawable.ic_medium_snow;
        } else if (weatherString.equals("大雪")) { //大雪
            resId = R.drawable.ic_heavy_snow;
        } else if (weatherString.equals("雾")) { //雾 、 霾
            resId = R.drawable.ic_fog;
        } else {
            resId = R.drawable.ic_unknown;
        }

        return resId;

    }

    /**
     * 将相同天气进行分组
     * First 为相同天气的数量
     * Second 为天气情况
     */
    private void initSameWeatherGroup() {
        mSameWeatherGroupData.clear();
        String currentLastWeather = "";
        int count = 0;
        for (int i = 0; i < mHourlyForecastList.size(); i++) {
            HourlyForecast forecast = mHourlyForecastList.get(i);
            if (i == 0) {
                currentLastWeather = forecast.condition.conditon;
            }
            if (forecast.condition.conditon != currentLastWeather) {
                Pair<Integer, String> pair = new Pair<>(count, currentLastWeather);
                mSameWeatherGroupData.add(pair);
                count = 1;
            } else {
                count++;
            }

            currentLastWeather = forecast.condition.conditon;

            if (i == mHourlyForecastList.size() - 1) {
                Pair<Integer, String> pair = new Pair<>(count, currentLastWeather);
                mSameWeatherGroupData.add(pair);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {
            mViewHeight = Math.max(heightSize, mMinViewHeight);
        } else {
            mViewHeight = mMinViewHeight;
        }

        int totalWidth = 0;
        if (mHourlyForecastList.size() > 1) {
            totalWidth = 2 * mDefaultPadding + mTimeLineInterval * (mHourlyForecastList.size() - 1);
        }
        mViewWidth = Math.max(mScreenWidth, totalWidth);   //默认控件最小宽度为屏幕宽度

        setMeasuredDimension(mViewWidth, mViewHeight);
        calculatePointGap();
        Log.d("onMeasure", "viewHeight = " + mViewHeight + ";viewWidth = " + mViewWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initDefaultPreferences(getContext());
        calculatePointGap();
    }

    /**
     * 计算高度差
     */
    private void calculatePointGap() {
        int lastMaxTem = -100000;
        int lastMinTem = 100000;
        for (HourlyForecast forecast : mHourlyForecastList) {
            if (Integer.parseInt(forecast.temperature) > lastMaxTem) {
                mMaxTemperature = Integer.parseInt(forecast.temperature);
                lastMaxTem = Integer.parseInt(forecast.temperature);
            }
            if (Integer.parseInt(forecast.temperature) < lastMinTem) {
                mMinTemperature = Integer.parseInt(forecast.temperature);
                lastMinTem = Integer.parseInt(forecast.temperature);
            }
        }

        float gap = (mMaxTemperature - mMinTemperature) * 1.0f;
        gap = (gap == 0.0f ? 1.0f : gap);  //保证分母不为0
        mPointGap = (mViewHeight - mMinPointHeight - 2 * mDefaultPadding) / gap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mHourlyForecastList.isEmpty()) {
            return;
        }

        drawAxis(canvas); // 画时间轴


    }

    private void drawAxis(Canvas canvas) {
        canvas.save();

        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setStrokeWidth(dp2px(getContext(), 1));

        canvas.drawLine(mDefaultPadding, mViewHeight - mDefaultPadding,
                mViewWidth - mDefaultPadding, mViewHeight - mDefaultPadding,
                mLinePaint);

        float centerY = mViewHeight - mDefaultPadding + dp2pxF(getContext(), 15);
        float centerX;
        for (int i = 0; i < mHourlyForecastList.size(); i++) {
            String text = mHourlyForecastList.get(i).date.split(" ")[1];
            centerX = mDefaultPadding + i * mTimeLineInterval;
            Paint.FontMetrics m = mTextPaint.getFontMetrics();
            canvas.drawText(text, 0, text.length(), centerX, centerY - (m.ascent + m.descent) / 2, mTextPaint);
        }
        canvas.restore();
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
