package com.stories.sunny;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stories.sunny.bean.WeatherBean;
import com.stories.sunny.custom_view.CircleProgressView;
import com.stories.sunny.custom_view.LineCharView;
import com.stories.sunny.db_model.CityStoraged;
import com.stories.sunny.gson_model.DailyForecast;
import com.stories.sunny.gson_model.Weather;
import com.stories.sunny.util.HttpUtil;
import com.stories.sunny.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Charlottecao on 9/8/17.
 */

public class WeatherFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "WeatherFragment";

    String weatherId;

    private List<CityStoraged> cityStoragedList;

    private ScrollView mainLayout;

    private SwipeRefreshLayout swipeRefresher;

    private Button cityManagerButton, mSettingButton;


    /**
     * Now
     */
    private TextView currentDegree;
    private TextView currentCity;
    private TextView updateTime;
    private TextView currentWeatherTxt;
    private TextView mCurrentAQI;
    private ImageView mCurrentWeatherIcon;
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
    private int mDailyForecastWeatherCode;
    private LinearLayout mDailyForecastLayout;
    private ImageView mDailyForecastIcon;
    private TextView mDailyForecastDate;
    private TextView mDailyForecastMaxMinDegree;
    private TextView mDailyForecastPrecipitationProbability;
    private TextView mDailyForecastInfo;
    private CardView mDailyForecastCardView;

    /**
     * Hourly
     */
    private List<WeatherBean> mWeatherBeanList = new ArrayList<>(); // 小时预报数据源
    private LineCharView mHourlyForecastLineCharView;
    private boolean mFirstFlag = true; // 是否是当天第一次写入，
    private int mMaxSize; // 小时预报写入文件的最大数量

    /**
     * Air quality
     */
    private CircleProgressView mAirQualityView;
    private TextView mCOTextView;
    private TextView mNO2TextView;
    private TextView mO3TextView;
    private TextView mPM10TextView;
    private TextView mPm25TextView;
    private TextView mSO2TextView;
    private ProgressBar mCOProgressBar;
    private ProgressBar mNO2ProgressBar;
    private ProgressBar mO3ProgressBar;
    private ProgressBar mPM10ProgressBar;
    private ProgressBar mPM25ProgressBar;
    private ProgressBar mSO2ProgressBar;
    private CardView mAirQualityCardView;

    /**
     * Astronomy
     */
    private ImageView mMoonImageView;
    private ImageView mSunImageView;
    private TextView mMoonTimeTextView;
    private TextView mMoonInfoTextView;
    private TextView mSunTimeTextView;
    private TextView mSunInfoTextView;


    /**
     *  Start
     */
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

       // mHourlyForecastLineCharView = (LineCharView) view.findViewById(R.id.hourly_forecast_line_chart);

        currentDegree = (TextView) view.findViewById(R.id.forecast_now_degree);
        currentCity = (TextView) view.findViewById(R.id.title_city);
        updateTime = (TextView) view.findViewById(R.id.title_update_time);
        currentWeatherTxt = (TextView) view.findViewById(R.id.forecast_now_info);
        mCurrentWeatherIcon = (ImageView) view.findViewById(R.id.forecast_now_icon);
        titleBackground = (ImageView) view.findViewById(R.id.title_bg);
        currentWindSpeed = (TextView) view.findViewById(R.id.forecast_now_wind_speed);
        currentWindDir = (TextView) view.findViewById(R.id.forecast_now_wind_dir);
        currentPrecipitation = (TextView) view.findViewById(R.id.forecast_now_precipitation);
        currentPrecipitationProbability = (TextView) view.findViewById(R.id.forecast_now_precipitation_probability);
        currentRealFeel = (TextView) view.findViewById(R.id.forecast_now_real_feel_degree);
        currentAtmosphericPressure = (TextView) view.findViewById(R.id.forecast_now_atmospheric_pressure);
        currentVisibility = (TextView) view.findViewById(R.id.forecast_now_visibility);
        currentRelativeHumidity = (TextView) view.findViewById(R.id.forecast_now_relative_humidity);
        mCurrentAQI = (TextView) view.findViewById(R.id.forecast_now_air_quality);

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

        mAirQualityView = (CircleProgressView) view.findViewById(R.id.air_progress);
        mCOTextView = (TextView) view.findViewById(R.id.air_quality_co);
        mNO2TextView = (TextView) view.findViewById(R.id.air_quality_no2);
        mO3TextView = (TextView) view.findViewById(R.id.air_quality_o3);
        mPM10TextView = (TextView) view.findViewById(R.id.air_quality_pm10);
        mPm25TextView = (TextView) view.findViewById(R.id.air_quality_pm25);
        mSO2TextView = (TextView) view.findViewById(R.id.air_quality_so2);
        mCOProgressBar = (ProgressBar) view.findViewById(R.id.air_progressBar_co);
        mNO2ProgressBar = (ProgressBar) view.findViewById(R.id.air_progressBar_no2);
        mO3ProgressBar = (ProgressBar) view.findViewById(R.id.air_progressBar_o3);
        mPM10ProgressBar = (ProgressBar) view.findViewById(R.id.air_progressBar_pm10);
        mPM25ProgressBar = (ProgressBar) view.findViewById(R.id.air_progressBar_pm25);
        mSO2ProgressBar = (ProgressBar) view.findViewById(R.id.air_progressBar_so2);
        mAirQualityCardView = (CardView) view.findViewById(R.id.air_quality_card_view);

        mMoonImageView = (ImageView) view.findViewById(R.id.forecast_now_moon_icon);
        mSunImageView = (ImageView) view.findViewById(R.id.forecast_now_sun_icon);
        mMoonTimeTextView = (TextView) view.findViewById(R.id.forecast_now_moon_time);
        mMoonInfoTextView = (TextView) view.findViewById(R.id.forecast_now_moon_info);
        mSunTimeTextView = (TextView) view.findViewById(R.id.forecast_now_sun_time);
        mSunInfoTextView = (TextView) view.findViewById(R.id.forecast_now_sun_info);

        /* ****** */
        cityManagerButton = (Button) view.findViewById(R.id.place_manager);
        cityManagerButton.setOnClickListener(this);

        /* ****** */
        mSettingButton = (Button) view.findViewById(R.id.setting);
        mSettingButton.setOnClickListener(this);



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
            case R.id.place_manager:
                startActivity(new Intent(getActivity(), CityManagerActivity.class));
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
                            putHourlyForecastDataIn(weather); //将小时天气写入文件
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

    /**
     * 新建存放小时预报数据的文件
     * 不同的时间更新天气小时预报会越来越少
     */
    private void putHourlyForecastDataIn(Weather weather) {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(getArguments().getString("city_name") + "HourlyForecastData", Context.MODE_PRIVATE).edit();
        for (int i = 0; i < weather.hourlyForecastList.size(); i++) {
            editor.putString(weather.hourlyForecastList.get(i).date.split(" ")[1], weather.hourlyForecastList.get(i).temperature + "-" + weather.hourlyForecastList.get(i).condition.code + "-" + weather.hourlyForecastList.get(i).condition.conditon);
            editor.apply();
        }
        if (mFirstFlag) {
            mMaxSize = weather.hourlyForecastList.size();
            mFirstFlag = false;
        }
    }

    /**
     * Display detailed info about weather
     * @param weather
     */
    private void showWeather(Weather weather) {

        mDailyForecastToday = weather.dailyForecastList.get(0);

        Intent intent = new Intent(getActivity(), AutoUpdayeService.class);
        getActivity().startService(intent);

        /**
         * Now Info
         */
        String currentCityName = weather.basic.cityName;
        String updateTimeData = weather.basic.update.updateTime.split(" ")[1]; //12:00
        String currentDegreeData = weather.now.Temperature;
        String currentWeatherTxtData = weather.now.condition.weatherInfo;
        String currentAQI = weather.aqi.city.aqi;
        int currentWeatherCode = Integer.valueOf(weather.now.condition.code).intValue();
        String currentWindSpeedData = weather.now.wind.windSpeed + " km/h";
        String currentWindDirData = weather.now.wind.direction;
        String currentPrecipitationData = weather.now.precipitation + " mm";
        String currentPrecipitationProbabilityData = mDailyForecastToday.precipitationProbability + " %";
        String currentRealFeelData = weather.now.realFeel + " °C";
        String currentAtmosphericPressureData = weather.now.atmosphericPressure + " mb";
        String currentVisibilityData = weather.now.visibility + " km";
        String currentRelativeHumidityData = weather.now.relativeHumidity + " %";

        /**
         * Astronomy
         */
        if (Integer.parseInt(weather.basic.update.updateTime.split(" ")[1].split(":")[0]) <= 18 && Integer.parseInt(weather.basic.update.updateTime.split(" ")[1].split(":")[0]) >= 10) { //显示日落  月初  时间
            Glide.with(getActivity()).load(R.drawable.ic_sunset).into(mSunImageView);
            mSunTimeTextView.setText(weather.dailyForecastList.get(0).astronomy.sunset);
            mSunInfoTextView.setText("日落时间");

            Glide.with(getActivity()).load(R.drawable.ic_moonrise).into(mMoonImageView);
            mMoonTimeTextView.setText(weather.dailyForecastList.get(0).astronomy.moonrise);
            mMoonInfoTextView.setText("月出时间");
        } else {
            Glide.with(getActivity()).load(R.drawable.ic_sunrise).into(mSunImageView);
            mSunTimeTextView.setText(weather.dailyForecastList.get(0).astronomy.sunrise);
            mSunInfoTextView.setText("日出时间");

            Glide.with(getActivity()).load(R.drawable.ic_moonset).into(mMoonImageView);
            mMoonTimeTextView.setText(weather.dailyForecastList.get(0).astronomy.moonset);
            mMoonInfoTextView.setText("月落时间");
        }

        /**
         * 为了将温度信息传递到城市管理页面
         */
        CityStoraged city = new CityStoraged();
        city.setMaxMinDegree(mDailyForecastToday.temperature.min + "~" + mDailyForecastToday.temperature.max + "°C");

        if (currentWeatherCode == 100) { // 晴
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_sunny);
            city.setWeatherImgId(R.drawable.ic_sunny);
        } else if (currentWeatherCode >= 101  && currentWeatherCode <= 104) { //阴
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_cloud);
            city.setWeatherImgId(R.drawable.ic_cloud);
        } else if (currentWeatherCode >= 200 && currentWeatherCode <= 213) { //风
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_wind);
            city.setWeatherImgId(R.drawable.ic_wind);
        } else if ((currentWeatherCode >= 300 && currentWeatherCode <= 301) || currentWeatherCode == 305 || currentWeatherCode == 309) { //阵雨
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_light_rain);
            city.setWeatherImgId(R.drawable.ic_light_rain);
        } else if (currentWeatherCode >= 302 && currentWeatherCode <= 304) { //雷阵雨
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_thunder);
            city.setWeatherImgId(R.drawable.ic_thunder);
        } else if ((currentWeatherCode >= 306 && currentWeatherCode <= 308) || (currentWeatherCode >= 310 && currentWeatherCode <= 313)) { //大（暴）雨
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_heavy_rain);
            city.setWeatherImgId(R.drawable.ic_heavy_rain);
        } else if (currentWeatherCode == 400 || currentWeatherCode == 406 || currentWeatherCode == 407) { //小雪 阵雪
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_light_snow);
            city.setWeatherImgId(R.drawable.ic_light_snow);
        } else if (currentWeatherCode == 401) { //中雪
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_medium_snow);
            city.setWeatherImgId(R.drawable.ic_medium_snow);
        } else if (currentWeatherCode >= 402 && currentWeatherCode <= 405) { //大雪
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_heavy_snow);
            city.setWeatherImgId(R.drawable.ic_heavy_snow);
        } else if (currentWeatherCode >= 500 && currentWeatherCode <= 508) { //雾 、 霾
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_fog);
            city.setWeatherImgId(R.drawable.ic_fog);
        } else {
            mCurrentWeatherIcon.setImageResource(R.drawable.ic_unknown);
            city.setWeatherImgId(R.drawable.ic_unknown);
        }

        city.updateAll("citystoragedname = ?", weather.basic.cityName);

        currentCity.setText(currentCityName);
        updateTime.setText(updateTimeData);
        currentDegree.setText(currentDegreeData);
        currentWeatherTxt.setText(currentWeatherTxtData);
        mCurrentAQI.setText("|  AQI:" + currentAQI + " >");
        currentWindSpeed.setText(currentWindSpeedData);
        currentWindDir.setText(currentWindDirData);
        currentPrecipitation.setText(currentPrecipitationData);
        currentRealFeel.setText(currentRealFeelData);
        currentAtmosphericPressure.setText(currentAtmosphericPressureData);
        currentVisibility.setText(currentVisibilityData);
        currentRelativeHumidity.setText(currentRelativeHumidityData);
        currentPrecipitationProbability.setText(currentPrecipitationProbabilityData);

        /**
         * Title Background
         */
        if (updateTimeData.split(":")[0] != null) {
            if (Integer.parseInt(updateTimeData.split(":")[0]) >= 6 && Integer.parseInt(updateTimeData.split(":")[0]) <= 18) {
                Glide.with(getActivity()).load(R.drawable.bg_daylight).into(titleBackground);
            } else {
                Glide.with(getActivity()).load(R.drawable.bg_night).into(titleBackground);
            }
        }

        /**
         * Air Show
         */
        int aqi = Integer.parseInt(weather.aqi.city.aqi);
        mAirQualityView.setProgress(aqi);
        mAirQualityView.setTextString(weather.aqi.city.airQulity);

        mCOTextView.setText(weather.aqi.city.co);
        mNO2TextView.setText(weather.aqi.city.no2);
        mO3TextView.setText(weather.aqi.city.o3);
        mPM10TextView.setText(weather.aqi.city.pm10);
        mPm25TextView.setText(weather.aqi.city.pm25);
        mSO2TextView.setText(weather.aqi.city.so2);
        mCOProgressBar.setProgress(Integer.parseInt(weather.aqi.city.co));
        mNO2ProgressBar.setProgress(Integer.parseInt(weather.aqi.city.no2));
        mO3ProgressBar.setProgress(Integer.parseInt(weather.aqi.city.o3));
        mPM10ProgressBar.setProgress(Integer.parseInt(weather.aqi.city.pm10));
        mPM25ProgressBar.setProgress(Integer.parseInt(weather.aqi.city.pm25));
        mSO2ProgressBar.setProgress(Integer.parseInt(weather.aqi.city.so2));


        if ("优".equals(weather.aqi.city.airQulity)) {
            mAirQualityCardView.setCardBackgroundColor(getResources().getColor(R.color.air_A));
        } else if ("良".equals(weather.aqi.city.airQulity)) {
            mAirQualityCardView.setCardBackgroundColor(getResources().getColor(R.color.air_B));
        } else {
            mAirQualityCardView.setCardBackgroundColor(getResources().getColor(R.color.air_C));
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
            Log.d(TAG, "dailyForecastList:" + weather.dailyForecastList.size());

            View view = LayoutInflater.from(getContext()).inflate(R.layout.daily_forecast_item, mDailyForecastLayout, false);
            //init
            mDailyForecastIcon = (ImageView) view.findViewById(R.id.daily_forecast_icon);
            mDailyForecastDate = (TextView) view.findViewById(R.id.daily_forecast_date);
            mDailyForecastMaxMinDegree = (TextView) view.findViewById(R.id.daily_forecast_max_min_temp);
            mDailyForecastPrecipitationProbability = (TextView) view.findViewById(R.id.daily_forecast_precipitation_probability);
            mDailyForecastInfo = (TextView) view.findViewById(R.id.daily_forecast_info);
            mDailyForecastCardView = (CardView) view.findViewById(R.id.daily_forecast_item);

            mDailyForecastWeatherCode = Integer.parseInt(forecast.condition.code_d);
            if (mDailyForecastWeatherCode == 100) { // 晴
                mDailyForecastIcon.setImageResource(R.drawable.ic_sunny);
                mDailyForecastCardView.setCardBackgroundColor(getResources().getColor(R.color.sunny));
            } else if (mDailyForecastWeatherCode >= 101  && mDailyForecastWeatherCode <= 104) { //阴
                mDailyForecastIcon.setImageResource(R.drawable.ic_cloud);
                mDailyForecastCardView.setCardBackgroundColor(getResources().getColor(R.color.fog_or_cloud));
            } else if (mDailyForecastWeatherCode >= 200 && mDailyForecastWeatherCode <= 213) { //风
                mDailyForecastIcon.setImageResource(R.drawable.ic_wind);
                mDailyForecastCardView.setCardBackgroundColor(getResources().getColor(R.color.wind));
            } else if ((mDailyForecastWeatherCode >= 300 && mDailyForecastWeatherCode <= 301) || mDailyForecastWeatherCode == 305 || mDailyForecastWeatherCode == 309) { //阵雨
                mDailyForecastIcon.setImageResource(R.drawable.ic_light_rain);
                mDailyForecastCardView.setCardBackgroundColor(getResources().getColor(R.color.rain));
            } else if (mDailyForecastWeatherCode >= 302 && mDailyForecastWeatherCode <= 304) { //雷阵雨
                mDailyForecastIcon.setImageResource(R.drawable.ic_thunder);
                mDailyForecastCardView.setCardBackgroundColor(getResources().getColor(R.color.rain));
            } else if ((mDailyForecastWeatherCode >= 306 && mDailyForecastWeatherCode <= 308) || (mDailyForecastWeatherCode >= 310 && mDailyForecastWeatherCode <= 313)) { //大（暴）雨
                mDailyForecastIcon.setImageResource(R.drawable.ic_heavy_rain);
                mDailyForecastCardView.setCardBackgroundColor(getResources().getColor(R.color.rain));
            } else if (mDailyForecastWeatherCode == 400 || mDailyForecastWeatherCode == 406 || mDailyForecastWeatherCode == 407) { //小雪 阵雪
                mDailyForecastIcon.setImageResource(R.drawable.ic_light_snow);
            } else if (mDailyForecastWeatherCode == 401) { //中雪
                mDailyForecastIcon.setImageResource(R.drawable.ic_medium_snow);
            } else if (mDailyForecastWeatherCode >= 402 && mDailyForecastWeatherCode <= 405) { //大雪
                mDailyForecastIcon.setImageResource(R.drawable.ic_heavy_snow);
            } else if (mDailyForecastWeatherCode >= 500 && mDailyForecastWeatherCode <= 508) { //雾 、 霾
                mDailyForecastIcon.setImageResource(R.drawable.ic_fog);
                mDailyForecastCardView.setCardBackgroundColor(getResources().getColor(R.color.fog_or_cloud));
            } else {
                mDailyForecastIcon.setImageResource(R.drawable.ic_unknown);
            }

            mDailyForecastDate.setText(forecast.date.split("-")[2] + "号");
            mDailyForecastMaxMinDegree.setText(forecast.temperature.min + "~" + forecast.temperature.max + "°C");
            mDailyForecastPrecipitationProbability.setText(forecast.precipitationProbability + "%");
            mDailyForecastInfo.setText(mDailyForecastDate.getText() + "白天" + forecast.condition.dayConditon + "," + "夜晚" + forecast.condition.nightCondition + "。" + "最高气温" + forecast.temperature.max + "°C，" + forecast.wind.direction + " " +forecast.wind.windFore);

            mDailyForecastLayout.addView(view);
        }


        /**
         * Hourly
         */
        SharedPreferences preferences = getContext().getSharedPreferences(getArguments().getString("city_name") + "HourlyForecastData", Context.MODE_PRIVATE);
        

       // mHourlyForecastLineCharView.setData(weather.hourlyForecastList);





        mainLayout.setVisibility(View.VISIBLE);
    }

}

