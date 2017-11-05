package com.stories.sunny;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stories.sunny.gson_model.DailyForecast;
import com.stories.sunny.gson_model.Weather;
import com.stories.sunny.util.HttpUtil;
import com.stories.sunny.util.Utility;
import com.stories.sunny.util.IconAndColorUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Charlottecao on 10/28/17.
 */

public class DailyForecastDetailActivity extends BaseActivity{

    private static final String TAG = "ForecastDetailActivity";

    private LinearLayout mDailyForecastDetailLayout;
    
    private int mDailyForecastDetailWeatherCode;  //weather Code  天气状况代码
    
    private String mCurrentCityWeatherId;  //穿过来的的相应城市的 weatherId

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast_detail);
        
        mCurrentCityWeatherId = getIntent().getStringExtra("weather_id");
        
        mDailyForecastDetailLayout = (LinearLayout) findViewById(R.id.daily_forecast_detail_layout);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(DailyForecastDetailActivity.this);
        String detailJson = preferences.getString(mCurrentCityWeatherId + "detailDailyForecast", null);
        if (detailJson != null) {
            Weather weather = Utility.parseWeatherJson(detailJson);
            showDetailDailyForecast(weather);
        } else {
            requestFromServer(mCurrentCityWeatherId);
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
                            * 以当前活动的类名创建一个SharedPreferences的文件
                            * 专门存储详细的  未来几天天气预报  信息
                            **/
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(DailyForecastDetailActivity.this).edit();
                            editor.putString(mCurrentCityWeatherId + "detailDailyForecast", weatherJson);
                            editor.apply();
                            
                            showDetailDailyForecast(weather);
                        } else {
                            Log.e(TAG, "run: " + "获取详细天气预报信息失败");
                        }
                    }
                });
                
            }
        });
    }
    
    private void showDetailDailyForecast(Weather weather) {
        List<DailyForecast> dailyForecastDetailList = weather.dailyForecastList;
        for (DailyForecast dailyForecast : dailyForecastDetailList) {

            View view = LayoutInflater.from(this).inflate(R.layout.activity_daily_forecast_detail_item, mDailyForecastDetailLayout, false);

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
            circleImageView.setImageResource(IconAndColorUtils.getIcon(mDailyForecastDetailWeatherCode));

            forecastDate.setText(Date2Week(dailyForecast.date));
            minDegree.setText(dailyForecast.temperature.min);
            maxDegree.setText(dailyForecast.temperature.max);
            windSpeed.setText(dailyForecast.wind.windSpeed + "km/h");
            windDir.setText(dailyForecast.wind.direction);
            precipitationProbability.setText(dailyForecast.precipitationProbability + "%");
            precipitation.setText(dailyForecast.precipitation + "mm");
            visibility.setText(dailyForecast.visibility + "km");
            atmosphericPressure.setText(dailyForecast.atmosphericPressure + "kp");
            relativeHumidity.setText(dailyForecast.relativeHumidity + "%");

            mDailyForecastDetailLayout.addView(view);
        }
    }


    /**
     * 将 "2017-11-02" 字符串转化为 "星期四"
     * @param date
     * @return
     */
    private String Date2Week(@NonNull String date) {
        Log.d(TAG, "Date2Week: " + "argument " + date);
        String[] week = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunny"};
        Calendar calendar = Calendar.getInstance();
        int nowDay = calendar.get(Calendar.DATE); //本机 现在 日期
        Log.d(TAG, "Date2Week: " + "NowDay " + nowDay);
        String[] splitDate = date.split("-");

        /*
        一周 从 周天 开始，则返回值1为周天
        一周 从 周一 开始，则返回值1为周一
         */
        calendar.set(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]) - 1, Integer.parseInt(splitDate[2])); // 设置为 未来的 日期
        boolean isFirstSunday = (calendar.getFirstDayOfWeek() == Calendar.SUNDAY);
        Log.d(TAG, "Date2Week: " + "isFirstSunday " + isFirstSunday);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        if(isFirstSunday){
            weekDay = weekDay - 1;
            if(weekDay == 0){
                weekDay = 7;
            }
        }
        Log.d(TAG, "Date2Week: " + "week " + weekDay);
        if (nowDay == calendar.get(Calendar.DATE)) {
            return "Today";
        } else if (nowDay == (calendar.get(Calendar.DATE) - 1)) {
            return "Tomorrow";
        } else {
            return week[weekDay - 1];
        }
    }
}
