package com.stories.sunny.gson_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlottecao on 9/3/17.
 */

public class HourlyForecast {

    public String date;    //时间

    @SerializedName("hum")
    public String relativeHumidity;  //相对湿度%

    @SerializedName("pop")
    public String precipitationProbability;  //降水概率

    @SerializedName("pres")
    public String atmosphericPressure;    //气压值

    @SerializedName("tmp")
    public String temperature;   //温度

    @SerializedName("cond")
    public Condition condition; // 基本天气状况

    public Wind wind;  //风力状况

    public class Wind {

        @SerializedName("dir")
        public String direction;   //风向

        @SerializedName("sc")
        public String windFore;    //风力

        @SerializedName("spd")
        public String windSpeed;   //风速
    }

    public class Condition {

        @SerializedName("txt")
        public String conditon;
    }
}
