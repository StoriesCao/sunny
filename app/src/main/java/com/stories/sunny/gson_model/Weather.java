package com.stories.sunny.gson_model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Charlottecao on 9/5/17.
 */

public class Weather {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<DailyForecast> dailyForecastList;

    @SerializedName("hourly_forecast")
    public List<HourlyForecast> hourlyForecastList;

}
