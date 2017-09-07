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

    private TextView currentRealFeel;

    private TextView currentAtmosphericPressure;

    private TextView currentVisibility;

    private TextView currentRelativeHumidity;


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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherJSON = preferences.getString("weatherJSON", null);
        if (weatherJSON != null) {
            //have cache
            Weather weather = Utility.parseWeatherJson(weatherJSON);
            showWeather(weather);
        } else {
            //no cache and request from server

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
        String currentCityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1]; //12:00
        String currentDegree = weather.now.Temperature;
        String currentWeatherTxt = weather.now.condition.weatherInfo;
        int currentWeatherCode = Integer.valueOf(weather.now.condition.code).intValue();
        String currentWindSpeed = weather.now.wind.windSpeed;
        String currentWindDir = weather.now.wind.direction;
        String currentPrecipitation = weather.now.precipitation;
        String currentRealFeel = weather.now.realFeel;
        String currentAtmosphericPressure = weather.now.atmosphericPressure;
        String currentVisibility = weather.now.visibility;
        String currentRelativeHumidity = weather.now.relativeHumidity;



    }
}




















