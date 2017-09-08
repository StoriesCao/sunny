package com.stories.sunny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stories.sunny.gson_model.DailyForecast;
import com.stories.sunny.gson_model.Weather;
import com.stories.sunny.util.HttpUtil;
import com.stories.sunny.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button cityManagerButton;

    private TextView currentDegree;

    private TextView currentCity;

    private TextView updateTime;

    private TextView currentWeatherTxt;

    private TextView currentAQI;

    private ImageView currentWeatherIcon;

    private TextView currentWindSpeed;

    private TextView currentWindDir;

    private TextView currentPrecipitation;

    private TextView currentPrecipitationProbability;

    private TextView currentRealFeel;

    private TextView currentAtmosphericPressure;

    private TextView currentVisibility;

    private TextView currentRelativeHumidity;

    private TextView suggestionAirBrif;

    private TextView suggestionAirInfo;

    private TextView suggestionComfBrif;

    private TextView suggestionComfInfo;

    private TextView suggestionCarWashBrif;

    private TextView suggestionCarWashInfo;

    private TextView suggestionDressBrif;

    private TextView suggestionDressInfo;

    private TextView suggestionFluBrif;

    private TextView suggestionFluInfo;

    private TextView suggestionSportBrif;

    private TextView suggestionSportInfo;

    private TextView suggestionTravelBrif;

    private TextView suggestionTravelInfo;

    private TextView suggestionUvBrif;

    private TextView suggestionUvInfo;

    private DailyForecast dailyForecastToday;

    private DailyForecast dailyForecastTomorrow;

    private DailyForecast dailyForecastAfterTomorrow;

    private TextView dailyForecastTodayDate;

    private TextView dailyForecastTodayMaxMinDegree;

    private TextView dailyForecastTodayPrecipitationProbability;

    private TextView dailyForecastTodayInfo;

    private TextView dailyForecastTomorrowDate;

    private TextView dailyForecastTomorrowMaxMinDegree;

    private TextView dailyForecastTomorrowPrecipitationProbability;

    private TextView dailyForecastTomorrowInfo;

    private TextView dailyForecastAfterTomorrowDate;

    private TextView dailyForecastAfterTomorrowMaxMinDegree;

    private TextView dailyForecastAfterTomorrowPrecipitationProbability;

    private TextView dailyForecastAfterTomorrowInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* ****** */
        cityManagerButton = (Button) findViewById(R.id.place_manager);
        cityManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CityManagerActivity.class);
                startActivity(intent);
            }
        });

        /* ****** */
        currentAQI = (TextView) findViewById(R.id.forecast_now_air_quality);
        currentAQI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AQIActivity.class);
                startActivity(intent);
            }
        });

        currentDegree = (TextView) findViewById(R.id.forecast_now_degree);
        currentCity = (TextView) findViewById(R.id.title_city);
        updateTime = (TextView) findViewById(R.id.title_update_time);
        currentWeatherTxt = (TextView) findViewById(R.id.forecast_now_info);
        currentWeatherIcon = (ImageView) findViewById(R.id.forecast_now_icon);
        currentWindSpeed = (TextView) findViewById(R.id.forecast_now_wind_speed);
        currentWindDir = (TextView) findViewById(R.id.forecast_now_wind_dir);
        currentPrecipitation = (TextView) findViewById(R.id.forecast_now_precipitation);
        currentPrecipitationProbability = (TextView) findViewById(R.id.forecast_now_precipitation_probability);
        currentRealFeel = (TextView) findViewById(R.id.forecast_now_real_feel_degree);
        currentAtmosphericPressure = (TextView) findViewById(R.id.forecast_now_atmospheric_pressure);
        currentVisibility = (TextView) findViewById(R.id.forecast_now_visibility);
        currentRelativeHumidity = (TextView) findViewById(R.id.forecast_now_relative_humidity);
        suggestionAirBrif = (TextView) findViewById(R.id.suggestion_air_brif);
        suggestionAirInfo = (TextView) findViewById(R.id.suggestion_air_info);
        suggestionComfBrif = (TextView) findViewById(R.id.suggestion_comf_brif);
        suggestionComfInfo = (TextView) findViewById(R.id.suggestion_comf_info);
        suggestionCarWashBrif = (TextView) findViewById(R.id.suggestion_car_wash_brif);
        suggestionCarWashInfo = (TextView) findViewById(R.id.suggestion_car_wash_info);
        suggestionDressBrif = (TextView) findViewById(R.id.suggestion_dress_brif);
        suggestionDressInfo = (TextView) findViewById(R.id.suggestion_dress_info);
        suggestionFluBrif = (TextView) findViewById(R.id.suggestion_flu_brif);
        suggestionFluInfo = (TextView) findViewById(R.id.suggestion_flu_info);
        suggestionSportBrif = (TextView) findViewById(R.id.suggestion_sport_brif);
        suggestionSportInfo = (TextView) findViewById(R.id.suggestion_sport_info);
        suggestionTravelBrif = (TextView) findViewById(R.id.suggestion_travel_brif);
        suggestionTravelInfo = (TextView) findViewById(R.id.suggestion_travel_info);
        suggestionUvBrif = (TextView) findViewById(R.id.suggestion_uv_brif);
        suggestionUvInfo = (TextView) findViewById(R.id.suggestion_uv_info);
        dailyForecastTodayDate = (TextView) findViewById(R.id.daily_forecast_date_today);
        dailyForecastTodayMaxMinDegree = (TextView) findViewById(R.id.daily_forecast_today_max_min_temp);
        dailyForecastTodayPrecipitationProbability = (TextView) findViewById(R.id.daily_forecast_today_precipitation_probability);
        dailyForecastTodayInfo = (TextView) findViewById(R.id.daily_forecast_today_info);
        dailyForecastTomorrowDate = (TextView) findViewById(R.id.daily_forecast_date_tomorrow);
        dailyForecastTomorrowMaxMinDegree = (TextView) findViewById(R.id.daily_forecast_tomorrow_max_min_temp);
        dailyForecastTomorrowPrecipitationProbability = (TextView) findViewById(R.id.daily_forecast_tomorrow_precipitation_probability);
        dailyForecastTomorrowInfo = (TextView) findViewById(R.id.daily_forecast_tomorrow_info);
        dailyForecastAfterTomorrowDate = (TextView) findViewById(R.id.daily_forecast_date_after_tomorrow);
        dailyForecastAfterTomorrowMaxMinDegree = (TextView) findViewById(R.id.daily_forecast_after_tomorrow_max_min_temp);
        dailyForecastAfterTomorrowPrecipitationProbability = (TextView) findViewById(R.id.daily_forecast_after_tomorrow_precipitation_probability);
        dailyForecastAfterTomorrowInfo = (TextView) findViewById(R.id.daily_forecast_after_tomorrow_info);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherJSON = preferences.getString("weatherJSON", null);
        if (weatherJSON != null) {
            //have cache
            Weather weather = Utility.parseWeatherJson(weatherJSON);
            showWeather(weather);
        } else {
            //no cache and request from server
            String weatherId = "CN101050101";
            requestWeatherFromServer(weatherId);
        }
    }

    /**
     * Request Weather info (JSON) from server.
     * @param weatherId
     */
    public void requestWeatherFromServer(final String weatherId) {
        String requestURL = "https://free-api.heweather.com/v5/weather?city=" + weatherId + "&key=f34a94fd34384c72be8d8c45112c7bb5";
        HttpUtil.sendOkHttpRequest(requestURL, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "从服务器获取信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.parseWeatherJson(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                            editor.putString("weatherJSON", responseText);
                            editor.apply();
                            showWeather(weather);
                        } else {
                            Toast.makeText(MainActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void showWeather(Weather weather) {

        dailyForecastToday = weather.dailyForecastList.get(0);
        dailyForecastTomorrow = weather.dailyForecastList.get(1);
        dailyForecastAfterTomorrow = weather.dailyForecastList.get(2);

        /* Now */
        String currentCityName = weather.basic.cityName;
        String updateTimeData = weather.basic.update.updateTime.split(" ")[1]; //12:00
        String currentDegreeData = weather.now.Temperature;
        String currentWeatherTxtData = weather.now.condition.weatherInfo;
        int currentWeatherCode = Integer.valueOf(weather.now.condition.code).intValue();
        String currentWindSpeedData = weather.now.wind.windSpeed + " km/h";
        String currentWindDirData = weather.now.wind.direction;
        String currentPrecipitationData = weather.now.precipitation + " mm";
        String currentPrecipitationProbabilityData = dailyForecastToday.precipitationProbability + " %";
        String currentRealFeelData = weather.now.realFeel + " °C";
        String currentAtmosphericPressureData = weather.now.atmosphericPressure + " mb";
        String currentVisibilityData = weather.now.visibility + " km";
        String currentRelativeHumidityData = weather.now.relativeHumidity + " %";

        currentCity.setText(currentCityName);
        updateTime.setText(updateTimeData);
        currentDegree.setText(currentDegreeData);
        currentWeatherTxt.setText(currentWeatherTxtData);
        currentWindSpeed.setText(currentWindSpeedData);
        currentWindDir.setText(currentWindDirData);
        currentPrecipitation.setText(currentPrecipitationData);
        currentRealFeel.setText(currentRealFeelData);
        currentAtmosphericPressure.setText(currentAtmosphericPressureData);
        currentVisibility.setText(currentVisibilityData);
        currentRelativeHumidity.setText(currentRelativeHumidityData);
        currentPrecipitationProbability.setText(currentPrecipitationProbabilityData);

        /* suggestions */
        String comfortBrifData = "舒适度: " + weather.suggestion.comfort.briefInfo;
        String comfortData = weather.suggestion.comfort.info;
        String airConditionBrifData = "空气情况: " + weather.suggestion.air.brifInfo;
        String airConditionData = weather.suggestion.air.info;
        String carWashBrifData = "洗车建议: " + weather.suggestion.carWash.briefInfo;
        String carWashData = weather.suggestion.carWash.info;
        String dressBrifData = "穿衣建议: " + weather.suggestion.dress.briefInfo;
        String dressData = weather.suggestion.dress.info;
        String fluBrifData = "感冒指数: " + weather.suggestion.flu.briefInfo;
        String fluData = weather.suggestion.flu.info;
        String sportBrifData = "运动指数: " + weather.suggestion.sport.briefInfo;
        String sportData = weather.suggestion.sport.info;
        String travelBrifData = "旅行指数: " + weather.suggestion.travel.briefInfo;
        String travelData = weather.suggestion.travel.info;
        String uvBrifData = "紫外线指数: " + weather.suggestion.ultravioletRay.briefInfo;
        String uvData = weather.suggestion.ultravioletRay.info;

        suggestionAirBrif.setText(airConditionBrifData);
        suggestionAirInfo.setText(airConditionData);
        suggestionComfBrif.setText(comfortBrifData);
        suggestionComfInfo.setText(comfortData);
        suggestionCarWashBrif.setText(carWashBrifData);
        suggestionCarWashInfo.setText(carWashData);
        suggestionDressBrif.setText(dressBrifData);
        suggestionDressInfo.setText(dressData);
        suggestionFluBrif.setText(fluBrifData);
        suggestionFluInfo.setText(fluData);
        suggestionSportBrif.setText(sportBrifData);
        suggestionSportInfo.setText(sportData);
        suggestionTravelBrif.setText(travelBrifData);
        suggestionTravelInfo.setText(travelData);
        suggestionUvBrif.setText(uvBrifData);
        suggestionUvInfo.setText(uvData);

        /*daily forecast*/
        dailyForecastTodayDate.setText("今天");
        dailyForecastTodayMaxMinDegree.setText(dailyForecastToday.temperature.min + "~" + dailyForecastToday.temperature.max + "°C");
        dailyForecastTodayPrecipitationProbability.setText(dailyForecastToday.precipitationProbability);
        dailyForecastTodayInfo.setText("今天白天" + dailyForecastToday.condition.dayConditon + "," + "夜晚" + dailyForecastToday.condition.nightCondition + "。" + "最高气温" + dailyForecastToday.temperature.max + "°C，" + dailyForecastToday.wind.direction + dailyForecastToday.wind.windFore);
        dailyForecastTomorrowDate.setText("明天");
        dailyForecastTomorrowMaxMinDegree.setText(dailyForecastAfterTomorrow.temperature.min + "~" + dailyForecastTomorrow.temperature.max + "°C");
        dailyForecastTomorrowPrecipitationProbability.setText(dailyForecastAfterTomorrow.precipitationProbability);
        dailyForecastTomorrowInfo.setText("今天白天" + dailyForecastTomorrow.condition.dayConditon + "," + "夜晚" + dailyForecastTomorrow.condition.nightCondition + "。" + "最高气温" + dailyForecastTomorrow.temperature.max + "°C，" + dailyForecastTomorrow.wind.direction +dailyForecastTomorrow.wind.windFore);
        dailyForecastAfterTomorrowDate.setText("后天");
        dailyForecastAfterTomorrowMaxMinDegree.setText(dailyForecastAfterTomorrow.temperature.min + "~" + dailyForecastAfterTomorrow.temperature.max + "°C");
        dailyForecastAfterTomorrowPrecipitationProbability.setText(dailyForecastAfterTomorrow.precipitationProbability);
        dailyForecastAfterTomorrowInfo.setText("今天白天" + dailyForecastAfterTomorrow.condition.dayConditon + "," + "夜晚" + dailyForecastAfterTomorrow.condition.nightCondition + "。" + "最高气温" + dailyForecastAfterTomorrow.temperature.max + "°C，" + dailyForecastAfterTomorrow.wind.direction + dailyForecastAfterTomorrow.wind.windFore);

    }
}




















