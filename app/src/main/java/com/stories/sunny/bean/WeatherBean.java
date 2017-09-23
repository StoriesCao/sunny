package com.stories.sunny.bean;

/**
 * Created by Charlottecao on 9/22/17.
 */

public class WeatherBean {

    private String temperature;  //温度

    private String code; //天气信息代码

    private String condition; //天气情况表述

    private String time; //小时预报时间

    public WeatherBean(String temperature, String condition, String time, String code) {
        this.temperature = temperature + "°";
        this.condition = condition;
        this.time = time;
        this.code = code;
    }

    public String gettemperature() {
        return temperature;
    }

    public String getCode() {
        return code;
    }

    public String getCondition() {
        return condition;
    }

    public String getTime() {
        return time;
    }

}
