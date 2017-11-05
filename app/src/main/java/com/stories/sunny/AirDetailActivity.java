package com.stories.sunny;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.stories.sunny.custom_view.CircleProgressView;
import com.stories.sunny.gson_model.Weather;
import com.stories.sunny.util.HttpUtil;
import com.stories.sunny.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Charlottecao on 11/4/17.
 */

public class AirDetailActivity extends BaseActivity {

    private static final String TAG = "AirDetailActivity";

    private CardView mAirCardView;
    private CircleProgressView mAirQualityCircleView;
    private TextView mAQIIndex;
    private TextView mAirQualityInfo;
    private TextView mCOIndex;
    private TextView mNO2Index;
    private TextView mO3Index;
    private TextView mPM10Index;
    private TextView mPM25Index;
    private TextView mSO2Index;
    private ProgressBar mCOProgressBar;
    private ProgressBar mNO2ProgressBar;
    private ProgressBar mO3ProgressBar;
    private ProgressBar mPM10ProgressBar;
    private ProgressBar mPM25ProgressBar;
    private ProgressBar mSO2ProgressBar;

    private String mWeatherId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_detail);

        mWeatherId = getIntent().getStringExtra("weather_id");

//        init
        mAirCardView = (CardView) findViewById(R.id.air_detail_condition_card_view);
        mAirQualityCircleView = (CircleProgressView) findViewById(R.id.air_circle_progress);
        mAQIIndex = (TextView) findViewById(R.id.air_quality_aqi);
        mAirQualityInfo = (TextView) findViewById(R.id.air_quality_info);
        mCOIndex = (TextView) findViewById(R.id.air_quality_co);
        mNO2Index = (TextView) findViewById(R.id.air_quality_no2);
        mO3Index = (TextView) findViewById(R.id.air_quality_o3);
        mPM10Index = (TextView) findViewById(R.id.air_quality_pm10);
        mPM25Index = (TextView) findViewById(R.id.air_quality_pm25);
        mSO2Index = (TextView) findViewById(R.id.air_quality_so2);
        mCOProgressBar = (ProgressBar) findViewById(R.id.air_progressBar_co);
        mNO2ProgressBar = (ProgressBar) findViewById(R.id.air_progressBar_no2);
        mO3ProgressBar = (ProgressBar) findViewById(R.id.air_progressBar_o3);
        mPM10ProgressBar = (ProgressBar) findViewById(R.id.air_progressBar_pm10);
        mPM25ProgressBar = (ProgressBar) findViewById(R.id.air_progressBar_pm25);
        mSO2ProgressBar = (ProgressBar) findViewById(R.id.air_progressBar_so2);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AirDetailActivity.this);
        String detailJson = preferences.getString(mWeatherId + "AirQualityDetail", null);
        if (detailJson != null) {
            Weather weather = Utility.parseWeatherJson(detailJson);
            showAirInfo(weather);
        } else {
            requestFromServer(mWeatherId);
        }

    }

    private void requestFromServer(final String weatherId) {
        String requestURL = "https://free-api.heweather.com/v5/weather?city=" + weatherId + "&key=f34a94fd34384c72be8d8c45112c7bb5";
        HttpUtil.sendOkHttpRequest(requestURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String weatherJson = response.body().string();
                final Weather weather = Utility.parseWeatherJson(weatherJson);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            /*
                            * 以当前活动的类名 AirDetailActivity 创建一个SharedPreferences的文件
                            * 专门存储详细的 空气质量 信息
                            **/
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(AirDetailActivity.this).edit();
                            editor.putString(mWeatherId + "AirQualityDetail", weatherJson);
                            editor.apply();

                            showAirInfo(weather);
                        } else {
                            Log.e(TAG, "run: " + "获取详细空气质量信息失败");
                        }
                    }
                });

            }
        });
    }

    private void showAirInfo(Weather weather) {

        int aqi = Integer.parseInt(weather.aqi.city.aqi);
        mAQIIndex.setText("AQI:" + aqi);
        mAirQualityInfo.setText(weather.aqi.city.airQulity);
        mAirQualityCircleView.setProgress(aqi);
        mAirQualityCircleView.setTextString(" ");

        if (weather.aqi.city.co != null) {
            mCOProgressBar.setProgress((int) Float.parseFloat(weather.aqi.city.co));
            mCOIndex.setText(weather.aqi.city.co);
        }

        if (weather.aqi.city.o3 != null) {
            mO3ProgressBar.setProgress((int) Float.parseFloat(weather.aqi.city.o3));
            mO3Index.setText(weather.aqi.city.o3);
        }

        if (weather.aqi.city.no2 != null) {
            mNO2ProgressBar.setProgress((int) Float.parseFloat(weather.aqi.city.no2));
            mNO2Index.setText(weather.aqi.city.no2);
        }

        if (weather.aqi.city.pm10 != null) {
            mPM10ProgressBar.setProgress((int) Float.parseFloat(weather.aqi.city.pm10));
            mPM10Index.setText(weather.aqi.city.pm10);
        }

        if (weather.aqi.city.pm25 != null) {
            mPM25ProgressBar.setProgress((int) Float.parseFloat(weather.aqi.city.pm25));
            mPM25Index.setText(weather.aqi.city.pm25);
        }

        if (weather.aqi.city.so2 != null) {
            mSO2ProgressBar.setProgress((int) Float.parseFloat(weather.aqi.city.so2));
            mSO2Index.setText(weather.aqi.city.so2);
        }


        if ("优".equals(weather.aqi.city.airQulity)) {
            mAirCardView.setCardBackgroundColor(getResources().getColor(R.color.air_A));
        } else if ("良".equals(weather.aqi.city.airQulity)) {
            mAirCardView.setCardBackgroundColor(getResources().getColor(R.color.air_B));
        } else {
            mAirCardView.setCardBackgroundColor(getResources().getColor(R.color.air_C));
        }
    }
}
