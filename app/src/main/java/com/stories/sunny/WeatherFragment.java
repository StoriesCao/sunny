package com.stories.sunny;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.stories.sunny.adapter.HourlyForecstAdapter;
import com.stories.sunny.db_model.WeatherBean;
import com.stories.sunny.custom_view.LineCharView;
import com.stories.sunny.db_model.CityStoraged;
import com.stories.sunny.gson_model.DailyForecast;
import com.stories.sunny.gson_model.Weather;
import com.stories.sunny.util.HttpUtil;
import com.stories.sunny.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Charlottecao on 9/8/17.
 */

public class WeatherFragment extends Fragment {

    private static final String TAG = "WeatherFragment";

    String weatherId;

    private List<CityStoraged> cityStoragedList;

    private ScrollView mainLayout;

    private SwipeRefreshLayout swipeRefresher;


    /**
     * Now
     */
    private CardView mNowCardView;
    private TextView currentDegree;
    private TextView currentWeatherTxt;
    private TextView mCurrentAQI;
    private ImageView mCurrentWeatherIcon;
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
     * Daily Forecast Simple
     */
    private DailyForecast mDailyForecastToday; // 预报中第一天即今天，有今天的更多信息
    private int mDailyForecastWeatherCode;
    private LinearLayout mDailyForecastSimpleLayout;  //主布局，实现动态添加
    private ImageView mDailyForecastSimpleIcon;    //天气图标
    private TextView mDailyForecastSimpleMaxMinDegree;  //最低最高温度
    private TextView mDailyForecastSimpleWindSpeed;  //风速
    private TextView mDailyForecastSimplePrecipitationProbability; //降水概率
    private CardView mDailyForecastSimpleCardView;


    /**
     * Hourly
     */
    private List<WeatherBean> mWeatherBeanList = new ArrayList<>(); // 小时预报数据源
    private LineCharView mHourlyForecastLineCharView;
    private Weather mWeather;
    private RecyclerView mHourlyForecastRecyclerView;


    /**
     * Astronomy
     */
    private ImageView mMoonImageView;
    private ImageView mSunImageView;
    private TextView mMoonTimeTextView;
    private TextView mMoonInfoTextView;
    private TextView mSunTimeTextView;
    private TextView mSunInfoTextView;


    private MainActivity mActivity;

    private String mCityName;



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

        mActivity = (MainActivity) getActivity();

        weatherId = getArguments().getString("weather_id");

        /* Daily Forecast Simple*/
        mDailyForecastSimpleLayout = (LinearLayout) view.findViewById(R.id.daily_forecast_simple_layout);
        mDailyForecastSimpleCardView = (CardView) view.findViewById(R.id.daily_forecast_simple_card_view);
        mDailyForecastSimpleCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DailyForecastDetailActivity.class);
                intent.putExtra("weather_id", weatherId);
                startActivity(intent);
            }
        });

        mainLayout = (ScrollView) view.findViewById(R.id.main_weather_layout);

        mHourlyForecastLineCharView = (LineCharView) view.findViewById(R.id.hourly_forecast_line_chart);

        /**
         * Now
         */
        mNowCardView = (CardView) view.findViewById(R.id.forecast_now_main);
        mNowCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AirDetailActivity.class);
                intent.putExtra("weather_id", weatherId);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), mNowCardView, "AirShared").toBundle());
            }
        });
        currentDegree = (TextView) view.findViewById(R.id.forecast_now_degree);
        currentWeatherTxt = (TextView) view.findViewById(R.id.forecast_now_info);
        mCurrentWeatherIcon = (ImageView) view.findViewById(R.id.forecast_now_icon);
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


        mMoonImageView = (ImageView) view.findViewById(R.id.forecast_now_moon_icon);
        mSunImageView = (ImageView) view.findViewById(R.id.forecast_now_sun_icon);
        mMoonTimeTextView = (TextView) view.findViewById(R.id.forecast_now_moon_time);
        mMoonInfoTextView = (TextView) view.findViewById(R.id.forecast_now_moon_info);
        mSunTimeTextView = (TextView) view.findViewById(R.id.forecast_now_sun_time);
        mSunInfoTextView = (TextView) view.findViewById(R.id.forecast_now_sun_info);

//        Hourly
        mHourlyForecastRecyclerView = (RecyclerView) view.findViewById(R.id.hourly_forecast_recycle_view);



        /* ****** */
       /* mToolbar = (Toolbar) view.findViewById(R.id.weather_main_tool_bar);
        setHasOptionsMenu(true); //调用fragment的OnCreateOptionsMenu() 方法
         ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_add_location);
        }*/

         /* ****** */
        swipeRefresher = (SwipeRefreshLayout) view.findViewById(R.id.main_weather_refresher);
        swipeRefresher.setColorSchemeResources(R.color.dark);
        swipeRefresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeatherFromServer(weatherId);
                //requestHomePic();
            }
        });


        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                //让CityManagerActivity返回所点击的是第几个City
                getActivity().startActivityForResult(new Intent(getActivity(), CityManagerActivity.class), 1);
                break;
            default:
                break;
        }
        return true;
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
            Log.d(TAG, "onActivityCreated: " + "showWeather 被调用了");
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
        String picUrl = "http://guolin.tech/api/bing_pic";

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
                            putHourlyForecastDataIn(weather);  //将小时天气写入数据库
                            showWeather(weather);
                            MainActivity.mViewPaperAdapter.notifyDataSetChanged(); //刷新相应的天气页面
                        } else {
                            Toast.makeText(getActivity(), "获取天气信息失败", Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresher.setRefreshing(false);
                    }
                });
            }
        });

        HttpUtil.sendOkHttpRequest(picUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取首页图片失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String realBingPicAddress = response.body().string();
                if (getActivity() == null) {  // ???
                    return;
                } else {
                    Log.d(TAG,  getActivity() == null? "getActivity YES" : "NO");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("BingPic", Context.MODE_PRIVATE).edit();
                            Log.d(TAG, editor == null ? "editor YES" : "NO");
                            editor.putString("real_bing_pic_address", realBingPicAddress);
                            editor.apply();
                        }
                    });
                }
            }
        });
    }

    /**
     * 刷新以天气的时候，顺便刷新一下HomePic，如果有新图片的话
     */
   /* public void requestHomePic() {
        String picUrl = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(picUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取首页图片失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String realBingPicAddress = response.body().string();
                if (getActivity() == null) {  // ???
                    return;
                } else {
                    Log.d(TAG,  getActivity() == null? "getActivity YES" : "NO");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SharedPreferences.Editor editor = getActivity().getSharedPreferences("BingPic", Context.MODE_PRIVATE).edit();
                            Log.d(TAG, editor == null ? "editor YES" : "NO");
                            editor.putString("real_bing_pic_address", realBingPicAddress);
                            editor.apply();
                        }
                    });
                }
            }
        });
    }*/

    /**
     * 新建存放不同城市不同时间的小时预报数据
     * 因为不同的时间更新天气小时预报会越来越少
     */
    private void putHourlyForecastDataIn(Weather weather) {
        for (int i = 0; i < weather.hourlyForecastList.size(); i++) {
            //先查询数据库中是否有此城市此时间的数据
            List<WeatherBean> weatherBeanList = DataSupport.where("cityname = ? and time = ?", getArguments().getString("city_name"), weather.hourlyForecastList.get(i).date.split(" ")[1]).find(WeatherBean.class);
            if (weatherBeanList.isEmpty()) {  //没有的话直接添加
                WeatherBean weatherBean = new WeatherBean();
                weatherBean.setCityName(getArguments().getString("city_name"));
                weatherBean.setTime(weather.hourlyForecastList.get(i).date.split(" ")[1]);
                weatherBean.setTemperature(weather.hourlyForecastList.get(i).temperature);
                weatherBean.setCondition(weather.hourlyForecastList.get(i).condition.conditon);
                weatherBean.setCode(weather.hourlyForecastList.get(i).condition.code);
                weatherBean.save();
            } else {
                //不支持自定义主键
                //为防止重复，更新此条数据
                WeatherBean weatherBean = new WeatherBean();
                weatherBean.setTemperature(weather.hourlyForecastList.get(i).temperature);
                weatherBean.setCondition(weather.hourlyForecastList.get(i).condition.conditon);
                weatherBean.setCode(weather.hourlyForecastList.get(i).condition.code);
                weatherBean.updateAll("cityname = ? and time = ?", getArguments().getString("city_name"), weather.hourlyForecastList.get(i).date.split(" ")[1]);
            }
        }
    }


    /**
     * Display detailed info about weather
     * @param weather
     */
    private void showWeather(Weather weather) {

        mDailyForecastToday = weather.dailyForecastList.get(0);

        //mDailyForecastSimple2DetailList = weather.dailyForecastList; //设置传递的数据

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
            mSunInfoTextView.setText(R.string.sunset);

            Glide.with(getActivity()).load(R.drawable.ic_moonrise).into(mMoonImageView);
            mMoonTimeTextView.setText(weather.dailyForecastList.get(0).astronomy.moonrise);
            mMoonInfoTextView.setText(R.string.moonrise);
        } else {
            Glide.with(getActivity()).load(R.drawable.ic_sunrise).into(mSunImageView);
            mSunTimeTextView.setText(weather.dailyForecastList.get(0).astronomy.sunrise);
            mSunInfoTextView.setText(R.string.sunrise);

            Glide.with(getActivity()).load(R.drawable.ic_moonset).into(mMoonImageView);
            mMoonTimeTextView.setText(weather.dailyForecastList.get(0).astronomy.moonset);
            mMoonInfoTextView.setText(R.string.moonset);
        }

        /**
         * 为了将温度信息传递到城市管理页面
         */
        CityStoraged city = new CityStoraged();
        city.setMaxMinDegree(mDailyForecastToday.temperature.min + "~" + mDailyForecastToday.temperature.max + "°C");
        city.setUpdateTime(weather.basic.update.updateTime.split(" ")[1]);

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
         * Daily forecast simple
         */

        for (DailyForecast forecast : weather.dailyForecastList) {
            Log.d(TAG, "dailyForecastSize: " + weather.dailyForecastList.size());

            View view = LayoutInflater.from(getContext()).inflate(R.layout.daily_forecast_simple_item, mDailyForecastSimpleLayout, false);
            //init
            mDailyForecastSimpleIcon = (ImageView) view.findViewById(R.id.daily_forecast_simple_item_icon);
            mDailyForecastSimpleMaxMinDegree = (TextView) view.findViewById(R.id.daily_forecast_simple_item_temperature);
            mDailyForecastSimplePrecipitationProbability = (TextView) view.findViewById(R.id.daily_forecast_simple_item_precipitation);
            mDailyForecastSimpleWindSpeed = (TextView) view.findViewById(R.id.daily_forecast_simple_item_wind_speed);

            mDailyForecastWeatherCode = Integer.parseInt(forecast.condition.code_d);
            if (mDailyForecastWeatherCode == 100) { // 晴
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_sunny);
            } else if (mDailyForecastWeatherCode >= 101  && mDailyForecastWeatherCode <= 104) { //阴
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_cloud);
            } else if (mDailyForecastWeatherCode >= 200 && mDailyForecastWeatherCode <= 213) { //风
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_wind);
            } else if ((mDailyForecastWeatherCode >= 300 && mDailyForecastWeatherCode <= 301) || mDailyForecastWeatherCode == 305 || mDailyForecastWeatherCode == 309) { //阵雨
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_light_rain);
            } else if (mDailyForecastWeatherCode >= 302 && mDailyForecastWeatherCode <= 304) { //雷阵雨
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_thunder);
            } else if ((mDailyForecastWeatherCode >= 306 && mDailyForecastWeatherCode <= 308) || (mDailyForecastWeatherCode >= 310 && mDailyForecastWeatherCode <= 313)) { //大（暴）雨
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_heavy_rain);
            } else if (mDailyForecastWeatherCode == 400 || mDailyForecastWeatherCode == 406 || mDailyForecastWeatherCode == 407) { //小雪 阵雪
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_light_snow);
            } else if (mDailyForecastWeatherCode == 401) { //中雪
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_medium_snow);
            } else if (mDailyForecastWeatherCode >= 402 && mDailyForecastWeatherCode <= 405) { //大雪
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_heavy_snow);
            } else if (mDailyForecastWeatherCode >= 500 && mDailyForecastWeatherCode <= 508) { //雾 、 霾
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_fog);
            } else {
                mDailyForecastSimpleIcon.setImageResource(R.drawable.ic_unknown);
            }

            mDailyForecastSimpleMaxMinDegree.setText(forecast.temperature.min + " ~ " + forecast.temperature.max + "°");
            mDailyForecastSimplePrecipitationProbability.setText(forecast.precipitationProbability + "%");
            mDailyForecastSimpleWindSpeed.setText(forecast.wind.windSpeed + "km/h");
            //mDailyForecastInfo.setText(mDailyForecastDate.getText() + getResources().getString(R.string.day_light) + forecast.condition.dayConditon + "," + getResources().getString(R.string.night) + forecast.condition.nightCondition + getResources().getString(R.string.full_stop) + getResources().getString(R.string.max_temperature) + forecast.temperature.max + "°C，" + forecast.wind.direction + " " +forecast.wind.windFore);

            mDailyForecastSimpleLayout.addView(view);
        }


        /**
         * Hourly
         */

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHourlyForecastRecyclerView.setLayoutManager(layoutManager);
        HourlyForecstAdapter adapter = new HourlyForecstAdapter(weather.hourlyForecastList);
        mHourlyForecastRecyclerView.setAdapter(adapter);

        List<WeatherBean> currentCityHourlyForecastDataList = DataSupport.where("cityname = ?", getArguments().getString("city_name")).find(WeatherBean.class);
        Log.d(TAG, "当前城市的小时预报数据量：" + currentCityHourlyForecastDataList.size());

        Collections.sort(currentCityHourlyForecastDataList, new Comparator<WeatherBean>() {  //按照更新时间从小到大排序
            @Override
            public int compare(WeatherBean weatherBean1, WeatherBean weatherBean2) {
                int i = Integer.parseInt(weatherBean1.getTime().split(":")[0]) - Integer.parseInt(weatherBean2.getTime().split(":")[0]);
                return i;
            }
        });

        //检查一下是否正确
        for (WeatherBean weatherBean : currentCityHourlyForecastDataList) {
            Log.d(TAG, weatherBean.getCityName() + "-" + weatherBean.getCondition() + "-" + weatherBean.getTemperature() + "-" + weatherBean.getTime());
        }
        /*
        哈尔滨-晴-17-10:00
        哈尔滨-晴-20-13:00
        哈尔滨-小雨-19-16:00
        哈尔滨-晴-15-19:00
        哈尔滨-晴-12-22:00
         */
        mHourlyForecastLineCharView.setData(currentCityHourlyForecastDataList);

        mainLayout.setVisibility(View.VISIBLE);
    }

}

