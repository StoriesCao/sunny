package com.stories.sunny.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.stories.sunny.R;
import com.stories.sunny.db_model.WeatherBean;
import com.stories.sunny.gson_model.HourlyForecast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Charlottecao on 9/19/17.
 */

public class LineCharView extends View {

    private static final String TAG = "LineCharView";

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
    private float mIconWidth;  //天气图标的边长

    private int mLeftPadding;
    private int mRightPadding;
    private int mBottomPadding;
    private int mTopPadding;

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

    private List<WeatherBean> mHourlyForecastList = new ArrayList<>(); // 源数据集合
    private Map<String, Bitmap> mIcons = new HashMap<>();   //天气图标集合
    private List<PointF> mPoints = new ArrayList<>(); //折线拐点坐标集合


    /**
     * 设置源数据
     * @param weatherBeanList
     */
    public void setData(List<WeatherBean> weatherBeanList) {
        if (weatherBeanList == null || weatherBeanList.isEmpty()) {
            return;
        }

        mHourlyForecastList = weatherBeanList;
        mPoints.clear();

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
        mTimeLineInterval = (int) typedArray.getDimension(R.styleable.LineCharView_mTimeLineInterval, dp2px(context, 50));
        mBackgroundColor = typedArray.getColor(R.styleable.LineCharView_backgroundColor, Color.WHITE);
        mMinPointHeight = (int) typedArray.getDimension(R.styleable.LineCharView_minPointHeight, dp2pxF(context, 30));
        typedArray.recycle();

        setBackgroundColor(mBackgroundColor);

        initDefaultPreferences(context);

        initPaint(context);

        //initIcon(); ?????
    }

    /**
     * Initialize
     */
    private void initDefaultPreferences(Context context) {
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        Log.d(TAG, "screen width:" + mScreenWidth + "  screen height:" + mScreenHeight);

        mMinViewHeight = 3 * mMinPointHeight;
        mPointRadius = dp2pxF(context, 2.5F);
        mTextSize = sp2px(context, 10);
        mLeftPadding = mRightPadding = mTopPadding = (int) (0.5F * mMinPointHeight);  //默认0.5倍
        mBottomPadding = (int)(1.2F * mMinPointHeight);
        Log.d(TAG, "Pl: " + mLeftPadding + " Pr: " + mRightPadding + " Pt: " + mTopPadding + " Pb: " + mBottomPadding);
        mIconWidth = (1.0f / 3.0f) * mTimeLineInterval; //默认1/3倍
    }

    private void initPaint(Context context) {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStrokeWidth(dp2px(context, 1));
        mLinePaint.setColor(Color.BLACK);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextAlign(Paint.Align.CENTER); //

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setStrokeWidth(dp2px(context, 1));
    }

    private void initIcon() {
        mIcons.clear();
        for (int i = 0; i < mWeatherConditionsString.length; i++) {
            Bitmap bitmap = getIconBitmap(Integer.parseInt(mHourlyForecastList.get(i).getCode()), mIconWidth, mIconWidth);
            mIcons.put(mWeatherConditionsString[i], bitmap);  //???//
        }

    }

    /**
     *
     * @param weatherCode
     * @param requestW
     * @param requestH
     * @return
     */
    private Bitmap getIconBitmap(int weatherCode, float requestW, float requestH) {
        int resId = getIconResId(weatherCode);
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
     * @param weatherCode
     * @return resource Id
     */
    private int getIconResId(int weatherCode) {
        int resId;

        if (weatherCode == 100) { // 晴
            resId = R.drawable.ic_sunny;
        } else if (weatherCode >= 101 && weatherCode <= 104) { //阴
            resId = R.drawable.ic_cloud;
        } else if (weatherCode >= 200 && weatherCode <= 213) { //风
            resId = R.drawable.ic_wind;
        } else if ((weatherCode >= 300 && weatherCode <= 301) || weatherCode == 305 || weatherCode == 309) { //阵雨
            resId = R.drawable.ic_light_rain;
        } else if (weatherCode >= 302 && weatherCode <= 304) { //雷阵雨
            resId = R.drawable.ic_thunder;
        } else if ((weatherCode >= 306 && weatherCode <=308) || (weatherCode >= 310 && weatherCode <= 313)) { //大（暴）雨
            resId = R.drawable.ic_heavy_rain;
        } else if (weatherCode == 400 || weatherCode == 406 || weatherCode == 407) { //小雪 阵雪
            resId = R.drawable.ic_light_snow;
        } else if (weatherCode == 401) { //中雪
            resId = R.drawable.ic_medium_snow;
        } else if (weatherCode >= 402 && weatherCode <= 405) { //大雪
            resId = R.drawable.ic_heavy_snow;
        } else if (weatherCode >= 500 && weatherCode <= 508) { //雾 、 霾
            resId = R.drawable.ic_fog;
        } else {
            resId = R.drawable.ic_unknown;
        }

        return resId;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.EXACTLY) {    //确定控件的高度
            mViewHeight = Math.max(heightSize, mMinViewHeight);
        } else {
            mViewHeight = mMinViewHeight;
        }

        mViewWidth = (mHourlyForecastList.size() - 1) * mTimeLineInterval +  mLeftPadding + mRightPadding; //确定控件的宽度

        setMeasuredDimension(mViewWidth, mViewHeight);

        calculatePointGap();

        Log.d(TAG, "viewHeight = " + mViewHeight + "  viewWidth = " + mViewWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        initDefaultPreferences(getContext());
        calculatePointGap();
    }

    /**
     * 计算高度差 赋值 = mPointGap
     */
    private void calculatePointGap() {
        int lastMaxTem = -100;
        int lastMinTem = 100;
        for (WeatherBean weatherBean : mHourlyForecastList) {
            if (Integer.parseInt(weatherBean.getTemperature()) > lastMaxTem) {
                mMaxTemperature = Integer.parseInt(weatherBean.getTemperature());
                lastMaxTem = Integer.parseInt(weatherBean.getTemperature());
            }
            if (Integer.parseInt(weatherBean.getTemperature()) < lastMinTem) {
                mMinTemperature = Integer.parseInt(weatherBean.getTemperature());
                lastMinTem = Integer.parseInt(weatherBean.getTemperature());
            }
        }

        float tempGap = (mMaxTemperature - mMinTemperature) * 1.0f;
        tempGap = (tempGap == 0.0f ? 1.0f : tempGap);   //保证分母不为0
        mPointGap = (mViewHeight - mBottomPadding - mTopPadding - mMinPointHeight) / tempGap;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mHourlyForecastList.isEmpty()) {
            return;
        }
        //如果在xml中自定义padding，则更新
        int newPaddingLeft = getPaddingLeft();
        int newPaddingRight = getPaddingRight();
        int newPaddingBottom = getPaddingBottom();
        int newPaddingTop = getPaddingTop();
        if (newPaddingLeft > mLeftPadding) {
            mLeftPadding = newPaddingLeft;
        } else if (newPaddingRight > mRightPadding) {
            mRightPadding = newPaddingRight;
        } else if (newPaddingBottom > mBottomPadding) {
            mBottomPadding = newPaddingBottom;
        } else if (newPaddingTop > mTopPadding) {
            mTopPadding = newPaddingTop;
        }

        drawAxis(canvas); // 画时间轴

        drawLinesAndPoints(canvas); //画折线图和他的拐点

        drawTemperature(canvas);
        
        //drawDifferentWeatherLine(canvas);

        //drawWeatherIcon(canvas);
    }

    /**
     * 画时间坐标轴
     */
    private void drawAxis(Canvas canvas) {
        canvas.save();

        canvas.drawLine(mLeftPadding, mViewHeight - mBottomPadding,
                mViewWidth - mRightPadding, mViewHeight - mBottomPadding,
                mLinePaint);

        float textCenterY = mViewHeight - mBottomPadding + dp2pxF(getContext(), 15);
        float textCenterX;
        for (int i = 0; i < mHourlyForecastList.size(); i++) {
            String text = mHourlyForecastList.get(i).getTime();
            textCenterX = mLeftPadding + i * mTimeLineInterval;
            Paint.FontMetrics m = mTextPaint.getFontMetrics();

            canvas.drawText(text, 0, text.length(), textCenterX, textCenterY - (m.ascent + m.descent) / 2, mTextPaint);
        }
        canvas.restore();
    }

    /**
     * 画折线和它拐点的园
     */
    private void drawLinesAndPoints(Canvas canvas) {
        canvas.save();

        mLinePaint.setStrokeWidth(dp2pxF(getContext(), 1));
        mLinePaint.setStyle(Paint.Style.STROKE);

        Path linePath = new Path();  //用于绘制折线
        mPoints.clear();

        int baseHeight = mBottomPadding + mMinPointHeight;
        float centerX;
        float centerY;
        for (int i = 0; i < mHourlyForecastList.size(); i++) {
            int tempGap = (Integer.parseInt(mHourlyForecastList.get(i).getTemperature())) - mMinTemperature;
            centerY = (int) (mViewHeight - (baseHeight + tempGap * mPointGap));
            centerX = mLeftPadding + i * mTimeLineInterval;
            mPoints.add(new PointF(centerX, centerY));  //记录下折线拐点的x、y坐标
            if (i == 0) {
                linePath.moveTo(centerX, centerY);
            } else {
                linePath.lineTo(centerX, centerY);
            }
        }
        canvas.drawPath(linePath, mLinePaint);   //画出折线

        //接下来画折线拐点的圆
        float x, y;
        for (int i = 0; i < mPoints.size(); i++) {
            x = mPoints.get(i).x;
            y = mPoints.get(i).y;

            //先画一个颜色为背景颜色的实心园覆盖掉折线拐角
            mPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPointPaint.setColor(mBackgroundColor);
            canvas.drawCircle(x, y,
                    mPointRadius + dp2pxF(getContext(), 1),
                    mPointPaint);
            //再画出正常的空心园
            mPointPaint.setStyle(Paint.Style.STROKE);
            mPointPaint.setColor(Color.BLACK);
            canvas.drawCircle(x, y,
                    mPointRadius,
                    mPointPaint);
        }
        canvas.restore();
    }

    /**
     * 画温度描述值
     */
    private void drawTemperature(Canvas canvas) {
        canvas.save();

        mTextPaint.setTextSize(1.2f * mTextSize); //字体放大一丢丢
        float centerX;
        float centerY;
        String text;
        for (int i = 0; i < mPoints.size(); i++) {
            text = mHourlyForecastList.get(i).getTemperature() + "°";
            centerX = mPoints.get(i).x;
            centerY = mPoints.get(i).y - dp2pxF(getContext(), 15);  //向上移动一丢丢
            Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
            canvas.drawText(text,
                    centerX, centerY - (metrics.ascent + metrics.descent/2),
                    mTextPaint);
        }
        mTextPaint.setTextSize(mTextSize);
        canvas.restore();
    }

    /**
     * 画不同天气之间的虚线
     *//*
    private void drawDifferentWeatherLine(Canvas canvas) {
        canvas.save();
        
        mLinePaint.setColor(Color.GRAY);
        mLinePaint.setStrokeWidth(dp2pxF(getContext(), 0.5f));
        mLinePaint.setAlpha(0xcc);

        //设置画笔画出虚线
        float[] f = {dp2pxF(getContext(), 8), dp2pxF(getContext(), 2)};  //两个值分别为循环的实线长度、空白长度
        PathEffect pathEffect = new DashPathEffect(f, 0);
        mLinePaint.setPathEffect(pathEffect);

        mDifferentWeatherXData.clear();
        int interval = 0;
        float startX, startY, endX, endY;
        endY = mViewHeight - mDefaultPadding;

        //0坐标点的虚线手动画上
        canvas.drawLine(mDefaultPadding, mPoints.get(0).y + mPointRadius + dp2pxF(getContext(), 2),
                mDefaultPadding, endY,
                mLinePaint);

        mDifferentWeatherXData.add((float) mDefaultPadding);

        for (int i = 0; i < mSameWeatherGroupData.size(); i++) {
            interval += mSameWeatherGroupData.get(i).first;
            if(interval > mPoints.size()-1){
                interval = mPoints.size()-1;
            }
            startX = endX = mDefaultPadding + interval * mTimeLineInterval;
            startY = mPoints.get(interval).y + mPointRadius + dp2pxF(getContext(), 2);
            mDifferentWeatherXData.add(startX);

            canvas.drawLine(startX, startY, endX, endY, mLinePaint);
        }

        //这里注意一下，当最后一组的连续天气数为1时，是不需要计入虚线集合的，否则会多画一个天气图标
        //若不理解，可尝试去掉下面这块代码并观察运行效果
        if(mSameWeatherGroupData.get(mSameWeatherGroupData.size()-1).first == 1
                && mDifferentWeatherXData.size() > 1){
            mDifferentWeatherXData.remove(mDifferentWeatherXData.get(mDifferentWeatherXData.size()-1));
        }

        mLinePaint.setPathEffect(null);
        mLinePaint.setAlpha(0xff);
        canvas.restore();
    } */


    /**
     * 画天气图标和它下方文字
     * 若相邻虚线都在屏幕内，图标的x位置即在两虚线的中间
     * 若有一条虚线在屏幕外，图标的x位置即在屏幕边沿到另一条虚线的中间
     * 若两条都在屏幕外，图标x位置紧贴某一条虚线或屏幕中间
     *//*
    private void drawWeatherIcon(Canvas canvas) {
        canvas.save();

        mTextPaint.setTextSize(0.9f * mTextSize); //字体缩小一丢丢

        boolean leftUsedScreenLeft = false;
        boolean rightUsedScreenRight = false;

        int scrollX = getScrollX();  //范围控制在0 ~ viewWidth-screenWidth
        float left, right;
        float iconX, iconY;
        float textY;     //文字的x坐标跟图标是一样的，无需额外声明

        iconY = mViewHeight - (mDefaultPadding + mMinPointHeight / 2.0f);
        textY = iconY + mIconWidth / 2.0f + dp2pxF(getContext(), 10);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        for (int i = 0; i < mDifferentWeatherXData.size() - 1; i++) {
            left = mDifferentWeatherXData.get(i);
            right = mDifferentWeatherXData.get(i + 1);

            //以下校正的情况为：两条虚线都在屏幕内或只有一条在屏幕内

            if (left < scrollX &&    //仅左虚线在屏幕外
                    right < scrollX + mScreenWidth) {
                left = scrollX;
                leftUsedScreenLeft = true;
            }
            
            if (right > scrollX + mScreenWidth &&  //仅右虚线在屏幕外
                    left > scrollX) {
                right = scrollX + mScreenWidth;
                rightUsedScreenRight = true;
            }

            if (right - left > mIconWidth) {    //经过上述校正之后左右距离还大于图标宽度
                iconX = left + (right - left) / 2.0f;
            } else {                          //经过上述校正之后左右距离小于图标宽度，则贴着在屏幕内的虚线
                if (leftUsedScreenLeft) {
                    iconX = right - mIconWidth / 2.0f;
                } else {
                    iconX = left + mIconWidth / 2.0f;
                }
            }

            //以下校正的情况为：两条虚线都在屏幕之外

            if (right < scrollX) {  //两条都在屏幕左侧，图标紧贴右虚线
                iconX = right - mIconWidth / 2.0f;
            } else if (left > scrollX + mScreenWidth) {   //两条都在屏幕右侧，图标紧贴左虚线
                iconX = left + mIconWidth / 2.0f;
            } else if (left < scrollX && right > scrollX + mScreenWidth) {  //一条在屏幕左 一条在屏幕右，图标居中
                iconX = scrollX + (mScreenWidth / 2.0f);
            }


            Bitmap icon = mIcons.get(mSameWeatherGroupData.get(i).second);

            //经过上述校正之后可以得到图标和文字的绘制区域
            RectF iconRect = new RectF(iconX - mIconWidth / 2.0f,
                    iconY - mIconWidth / 2.0f,
                    iconX + mIconWidth / 2.0f,
                    iconY + mIconWidth / 2.0f);

            canvas.drawBitmap(icon, null, iconRect, null);  //画图标
            canvas.drawText(mSameWeatherGroupData.get(i).second, iconX,
                    textY - (metrics.ascent+metrics.descent)/2,
                    mTextPaint); //画图标下方文字

            leftUsedScreenLeft = rightUsedScreenRight = false; //重置标志位
        }

        mTextPaint.setTextSize(mTextSize);
        canvas.restore();
    }
*/
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
