package com.stories.sunny;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stories.sunny.gson_model.DailyForecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Charlottecao on 10/28/17.
 */

public class DailyForecastDetailActivity extends BaseActivity {

    private static final String TAG = "ForecastDetailActivity";

    private List<DailyForecast> mDailyForecastDetailList;
    
    private int mDailyForecastDetailWeatherCode;  //weather Code  天气状况代码

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast_detail);

        LinearLayout DailyForecastDetailLayout = (LinearLayout) findViewById(R.id.daily_forecast_detail_layout);
        mDailyForecastDetailList = WeatherFragment.mDailyForecastSimple2DetailList;  //拿到天气预报的详细数据

        for (DailyForecast dailyForecast : mDailyForecastDetailList) {
            View view = LayoutInflater.from(this).inflate(R.layout.activity_daily_forecast_detail_item, DailyForecastDetailLayout, false);

            TextView forecastDate = (TextView) view.findViewById(R.id.daily_forecast_detail_date);
            CircleImageView circleImageView = (CircleImageView) view.findViewById(R.id.daily_forecast_detail_icon);
            TextView minDegree = (TextView) view.findViewById(R.id.daily_forecast_detail_min_temperature);
            TextView maxDegree = (TextView) view.findViewById(R.id.daily_forecast_detail_max_temperature);
            TextView windDir = (TextView) view.findViewById(R.id.daily_forecast_detail_wind_dir);
            TextView windSpeed = (TextView) view.findViewById(R.id.daily_forecast_detail_wind_speed);
            TextView precipitation = (TextView) view.findViewById(R.id.daily_forecast_detail_precipitation);
            TextView precipitationProbability = (TextView) view.findViewById(R.id.daily_forecast_detail_precipitation_probability);
            TextView visibility = (TextView) view.findViewById(R.id.daily_forecast_detail_visibility);
            TextView atmosphericPressure = (TextView) view.findViewById(R.id.daily_forecast_detail_atmospheric_pressure);
            TextView relativeHumidity = (TextView) view.findViewById(R.id.daily_forecast_detail_relative_humidity);

            // 设置天气图标
            mDailyForecastDetailWeatherCode = Integer.parseInt(dailyForecast.condition.code_d);
            if (mDailyForecastDetailWeatherCode == 100) { // 晴
                circleImageView.setImageResource(R.drawable.ic_sunny);
            } else if (mDailyForecastDetailWeatherCode >= 101  && mDailyForecastDetailWeatherCode <= 104) { //阴
                circleImageView.setImageResource(R.drawable.ic_cloud);
            } else if (mDailyForecastDetailWeatherCode >= 200 && mDailyForecastDetailWeatherCode <= 213) { //风
                circleImageView.setImageResource(R.drawable.ic_wind);
            } else if ((mDailyForecastDetailWeatherCode >= 300 && mDailyForecastDetailWeatherCode <= 301) || mDailyForecastDetailWeatherCode == 305 || mDailyForecastDetailWeatherCode == 309) { //阵雨
                circleImageView.setImageResource(R.drawable.ic_light_rain);
            } else if (mDailyForecastDetailWeatherCode >= 302 && mDailyForecastDetailWeatherCode <= 304) { //雷阵雨
                circleImageView.setImageResource(R.drawable.ic_thunder);
            } else if ((mDailyForecastDetailWeatherCode >= 306 && mDailyForecastDetailWeatherCode <= 308) || (mDailyForecastDetailWeatherCode >= 310 && mDailyForecastDetailWeatherCode <= 313)) { //大（暴）雨
                circleImageView.setImageResource(R.drawable.ic_heavy_rain);
            } else if (mDailyForecastDetailWeatherCode == 400 || mDailyForecastDetailWeatherCode == 406 || mDailyForecastDetailWeatherCode == 407) { //小雪 阵雪
                circleImageView.setImageResource(R.drawable.ic_light_snow);
            } else if (mDailyForecastDetailWeatherCode == 401) { //中雪
                circleImageView.setImageResource(R.drawable.ic_medium_snow);
            } else if (mDailyForecastDetailWeatherCode >= 402 && mDailyForecastDetailWeatherCode <= 405) { //大雪
                circleImageView.setImageResource(R.drawable.ic_heavy_snow);
            } else if (mDailyForecastDetailWeatherCode >= 500 && mDailyForecastDetailWeatherCode <= 508) { //雾 、 霾
                circleImageView.setImageResource(R.drawable.ic_fog);
            } else {
                circleImageView.setImageResource(R.drawable.ic_unknown);
            }


            forecastDate.setText(dailyForecast.date);
            minDegree.setText(dailyForecast.temperature.min);
            maxDegree.setText(dailyForecast.temperature.max);
            windSpeed.setText(dailyForecast.wind.windSpeed + "km/h");
            windDir.setText(dailyForecast.wind.direction);
            precipitationProbability.setText(dailyForecast.precipitationProbability + "%");
            precipitation.setText(dailyForecast.precipitation + "mm");
            visibility.setText(dailyForecast.visibility + "km");
            atmosphericPressure.setText(dailyForecast.atmosphericPressure + "kp");
            relativeHumidity.setText(dailyForecast.relativeHumidity + "%");

            DailyForecastDetailLayout.addView(view);
        }

    }

}
