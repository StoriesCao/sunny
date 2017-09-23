package com.stories.sunny.db_model;

import org.litepal.crud.DataSupport;

/**
 * Created by Charlottecao on 9/22/17.
 */

public class WeatherBean extends DataSupport{

    private int id;

    private String cityName;

    private String temperature;  //温度

    private String code; //天气信息代码

    private String condition; //天气情况表述

    private String time; //小时预报时间

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
