package com.stories.sunny;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stories.sunny.db_model.CityStoraged;
import com.stories.sunny.gson_model.DailyForecast;
import com.stories.sunny.gson_model.Weather;
import com.stories.sunny.util.HttpUtil;
import com.stories.sunny.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Charlottecao on 9/8/17.
 */

public class WeatherFragment extends Fragment implements View.OnClickListener{

    String weatherId;

    private List<CityStoraged> cityStoragedList;

    private ScrollView mainLayout;

    private SwipeRefreshLayout swipeRefresher;

    private Button cityManagerButton, mSettingButton;

    private TextView currentDegree;

    private TextView currentCity;

    private TextView updateTime;

    private TextView currentWeatherTxt;

    private TextView currentAQI;

    private ImageView currentWeatherIcon;

    private ImageView titleBackground;

    private TextView currentWindSpeed;

    private TextView currentWindDir;

    private TextView currentPrecipitation;

    private TextView currentPrecipitationProbability;

    private TextView currentRealFeel;

    private TextView currentAtmosphericPressure;

    private TextView currentVisibility;

    private TextView currentRelativeHumidity;

    /**
     * Suggestions
     */
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

    /**
     * Daily Forecast
     */
    private DailyForecast mDailyForecastToday; // 预报中第一天即今天，有今天的更多信息
    private LinearLayout mDailyForecastLayout;
    private ImageView mDailyForecastIcon;
    private TextView mDailyForecastDate;
    private TextView mDailyForecastMaxMinDegree;
    private TextView mDailyForecastPrecipitationProbability;
    private TextView mDailyForecastInfo;
    private CardView mDailyForecastCardView;


    /* start */
    public static WeatherFragment newInstance(String weatherId, String cityName) {
        Bundle args = new Bundle();
        args.putString("weather_id", weatherId);
        args.putString("city_name", cityName);
        WeatherFragment weatherFragment = new WeatherFragment();
        weatherFragment.setArguments(args);
        return weatherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_main, container, false);

        weatherId = getArguments().getString("weather_id");

        mDailyForecastLayout = (LinearLayout) view.findViewById(R.id.daily_forecast_layout);
        mainLayout = (ScrollView) view.findViewById(R.id.main_weather_layout);

        currentDegree = (TextView) view.findViewById(R.id.forecast_now_degree);
        currentCity = (TextView) view.findViewById(R.id.title_city);
        updateTime = (TextView) view.findViewById(R.id.title_update_time);
        currentWeatherTxt = (TextView) view.findViewById(R.id.forecast_now_info);
        currentWeatherIcon = (ImageView) view.findViewById(R.id.forecast_now_icon);
        titleBackground = (ImageView) view.findViewById(R.id.title_bg);
        currentWindSpeed = (TextView) view.findViewById(R.id.forecast_now_wind_speed);
        currentWindDir = (TextView) view.findViewById(R.id.forecast_now_wind_dir);
        currentPrecipitation = (TextView) view.findViewById(R.id.forecast_now_precipitation);
        currentPrecipitationProbability = (TextView) view.findViewById(R.id.forecast_now_precipitation_probability);
        currentRealFeel = (TextView) view.findViewById(R.id.forecast_now_real_feel_degree);
        currentAtmosphericPressure = (TextView) view.findViewById(R.id.forecast_now_atmospheric_pressure);
        currentVisibility = (TextView) view.findViewById(R.id.forecast_now_visibility);
        currentRelativeHumidity = (TextView) view.findViewById(R.id.forecast_now_relative_humidity);

        suggestionAirBrif = (TextView) view.findViewById(R.id.suggestion_air_brif);
        suggestionAirInfo = (TextView) view.findViewById(R.id.suggestion_air_info);
        suggestionComfBrif = (TextView) view.findViewById(R.id.suggestion_comf_brif);
        suggestionComfInfo = (TextView) view.findViewById(R.id.suggestion_comf_info);
        suggestionCarWashBrif = (TextView) view.findViewById(R.id.suggestion_car_wash_brif);
        suggestionCarWashInfo = (TextView) view.findViewById(R.id.suggestion_car_wash_info);
        suggestionDressBrif = (TextView) view.findViewById(R.id.suggestion_dress_brif);
        suggestionDressInfo = (TextView) view.findViewById(R.id.suggestion_dress_info);
        suggestionFluBrif = (TextView) view.findViewById(R.id.suggestion_flu_brif);
        suggestionFluInfo = (TextView) view.findViewById(R.id.suggestion_flu_info);
        suggestionSportBrif = (TextView) view.findViewById(R.id.suggestion_sport_brif);
        suggestionSportInfo = (TextView) view.findViewById(R.id.suggestion_sport_info);
        suggestionTravelBrif = (TextView) view.findViewById(R.id.suggestion_travel_brif);
        suggestionTravelInfo = (TextView) view.findViewById(R.id.suggestion_travel_info);
        suggestionUvBrif = (TextView) view.findViewById(R.id.suggestion_uv_brif);
        suggestionUvInfo = (TextView) view.findViewById(R.id.suggestion_uv_info);


        /* ****** */
        cityManagerButton = (Button) view.findViewById(R.id.place_manager);
        cityManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CityManagerActivity.class);
                startActivity(intent);
            }
        });

        /**
         *
         */
        mSettingButton = (Button) view.findViewById(R.id.setting);
        mSettingButton.setOnClickListener(this);

         /* ****** */
        currentAQI = (TextView) view.findViewById(R.id.forecast_now_air_quality);
        currentAQI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AQIActivity.class);
                startActivity(intent);
            }
        });

         /* ****** */
        swipeRefresher = (SwipeRefreshLayout) view.findViewById(R.id.main_weather_refresher);
        swipeRefresher.setColorSchemeResources(R.color.dark);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeatherFromServer(weatherId);
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cityStoragedList = DataSupport.findAll(CityStoraged.class); //查找用户是否存储了相关城市

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());  //是否存在当前城市的缓存
        String currentWeatherJSON = preferences.getString(getArguments().getString("city_name") + "weatherJSON", null);

        if (cityStoragedList.size() == 0) {  //用户并没有添加任何城市
            startActivity(new Intent(getActivity(), CityManagerActivity.class));
        } else if (currentWeatherJSON != null){
            //have cache
            Weather weather = Utility.parseWeatherJson(currentWeatherJSON);
            showWeather(weather);
        } else {
            //no cache and request from server
            mainLayout.setVisibility(View.INVISIBLE);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "从服务器获取信息失败", Toast.LENGTH_SHORT).show();
                        swipeRefresher.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.parseWeatherJson(responseText);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)) {
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getContext()).edit();
                            editor.putString(weather.basic.cityName + "weatherJSON", responseText);
                            editor.apply();
                            showWeather(weather);
                        } else {
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresher.setRefreshing(false);
                    }
                });
            }
        });

    }

    private void showWeather(Weather weather) {

        mDailyForecastToday = weather.dailyForecastList.get(0);

        /**
         * 为了将温度信息传递到城市管理页面
         */
        CityStoraged city = new CityStoraged();
        city.setMaxMinDegree(mDailyForecastToday.temperature.min + "~" + mDailyForecastToday.temperature.max + "°C");
        city.updateAll("citystoragedname = ?", weather.basic.cityName);

        Intent intent = new Intent(getActivity(), AutoUpdayeService.class);
        getActivity().startService(intent);

        /**
         * Now Info
         */
        String currentCityName = weather.basic.cityName;
        String updateTimeData = weather.basic.update.updateTime.split(" ")[1]; //12:00
        String currentDegreeData = weather.now.Temperature;
        String currentWeatherTxtData = weather.now.condition.weatherInfo;
        int currentWeatherCode = Integer.valueOf(weather.now.condition.code).intValue();
        String currentWindSpeedData = weather.now.wind.windSpeed + " km/h";
        String currentWindDirData = weather.now.wind.direction;
        String currentPrecipitationData = weather.now.precipitation + " mm";
        String currentPrecipitationProbabilityData = mDailyForecastToday.precipitationProbability + " %";
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

        if (updateTimeData.split(":")[0] != null) {
            if (Integer.parseInt(updateTimeData.split(":")[0]) >= 6 && Integer.parseInt(updateTimeData.split(":")[0]) <= 18) {
                Glide.with(getActivity()).load(R.drawable.bg_daylight).into(titleBackground);
            } else {
                Glide.with(getActivity()).load(R.drawable.bg_night).into(titleBackground);
            }
        }
        /**
         *  Suggestions
         */
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



        /**
         * daily forecast
         */
        for (DailyForecast forecast : weather.dailyForecastList) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.daily_forecast_item, mDailyForecastLayout, false);
            //init
            mDailyForecastIcon = (ImageView) view.findViewById(R.id.daily_forecast_icon);
            mDailyForecastDate = (TextView) view.findViewById(R.id.daily_forecast_date);
            mDailyForecastMaxMinDegree = (TextView) view.findViewById(R.id.daily_forecast_max_min_temp);
            mDailyForecastPrecipitationProbability = (TextView) view.findViewById(R.id.daily_forecast_precipitation_probability);
            mDailyForecastInfo = (TextView) view.findViewById(R.id.daily_forecast_info);
            mDailyForecastCardView = (CardView) view.findViewById(R.id.daily_forecast_item);

            mDailyForecastDate.setText(forecast.date.split("-")[2] + "号");
            mDailyForecastMaxMinDegree.setText(forecast.temperature.min + "~" + forecast.temperature.max + "°C");
            mDailyForecastPrecipitationProbability.setText(forecast.precipitationProbability + "%");
            mDailyForecastInfo.setText(mDailyForecastDate.getText() + "白天" + forecast.condition.dayConditon + "," + "夜晚" + forecast.condition.nightCondition + "。" + "最高气温" + forecast.temperature.max + "°C，" + forecast.wind.direction + " " +forecast.wind.windFore);

            mDailyForecastLayout.addView(view);
        }

        /* ***** */
       /* if (Integer.parseInt(dailyForecastToday.condition.code_d) >= 100 && Integer.parseInt(dailyForecastToday.condition.code_d) <= 204) {
            cardViewToday.setBackgroundColor(getResources().getColor(R.color.sunny));
        } else  if (Integer.parseInt(dailyForecastToday.condition.code_d) >= 205 && Integer.parseInt(dailyForecastToday.condition.code_d) <= 213) {
            cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.wind));
        } else if (Integer.parseInt(dailyForecastToday.condition.code_d) >= 300 && Integer.parseInt(dailyForecastToday.condition.code_d) <= 313) {
            cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.rain));
        } else if (Integer.parseInt(dailyForecastToday.condition.code_d) >= 500 && Integer.parseInt(dailyForecastToday.condition.code_d) <= 501) {
            cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.fog));
        }else if (Integer.parseInt(dailyForecastToday.condition.code_d) >= 502 && Integer.parseInt(dailyForecastToday.condition.code_d) <= 508) {
            cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.dust));
        }

        if (Integer.parseInt(dailyForecastTomorrow.condition.code_d) >= 100 && Integer.parseInt(dailyForecastTomorrow.condition.code_d) <= 204) {
            cardViewTomorrow.setBackgroundColor(getResources().getColor(R.color.sunny));
        } else  if (Integer.parseInt(dailyForecastTomorrow.condition.code_d) >= 205 && Integer.parseInt(dailyForecastTomorrow.condition.code_d) <= 213) {
            cardViewTomorrow.setCardBackgroundColor(getResources().getColor(R.color.wind));
        } else if (Integer.parseInt(dailyForecastTomorrow.condition.code_d) >= 300 && Integer.parseInt(dailyForecastTomorrow.condition.code_d) <= 313) {
            cardViewTomorrow.setCardBackgroundColor(getResources().getColor(R.color.rain));
        } else if (Integer.parseInt(dailyForecastTomorrow.condition.code_d) >= 500 && Integer.parseInt(dailyForecastTomorrow.condition.code_d) <= 501) {
            cardViewTomorrow.setCardBackgroundColor(getResources().getColor(R.color.fog));
        }else if (Integer.parseInt(dailyForecastTomorrow.condition.code_d) >= 502 && Integer.parseInt(dailyForecastTomorrow.condition.code_d) <= 508) {
            cardViewTomorrow.setCardBackgroundColor(getResources().getColor(R.color.dust));
        }

        if (Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) >= 100 && Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) <= 204) {
            cardViewAfterTomorrow.setBackgroundColor(getResources().getColor(R.color.sunny));
        } else  if (Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) >= 205 && Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) <= 213) {
            cardViewAfterTomorrow.setCardBackgroundColor(getResources().getColor(R.color.wind));
        } else if (Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) >= 300 && Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) <= 313) {
            cardViewAfterTomorrow.setCardBackgroundColor(getResources().getColor(R.color.rain));
        } else if (Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) >= 500 && Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) <= 501) {
            cardViewAfterTomorrow.setCardBackgroundColor(getResources().getColor(R.color.fog));
        }else if (Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) >= 502 && Integer.parseInt(dailyForecastAfterTomorrow.condition.code_d) <= 508) {
            cardViewAfterTomorrow.setCardBackgroundColor(getResources().getColor(R.color.dust));
        }
*/


        mainLayout.setVisibility(View.VISIBLE);
    }

}

