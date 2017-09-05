package com.stories.sunny.gson_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlottecao on 9/3/17.
 */

public class DailyForecast {

    public String date;

    @SerializedName("hum")
    public String relativeHumidity;  //相对湿度

    @SerializedName("pcpn")
    public String precipitation;   //降水量

    @SerializedName("pop")
    public String precipitationProbability;  //降水概率

    @SerializedName("pres")
    public String atmosphericPressure;  //气压

    @SerializedName("vis")
    public String visibility;  //能见度

    @SerializedName("tmp")
    public Temperature temperature;  //温度

    @SerializedName("cond")
    public Condition condition;  //基本天气信息

    @SerializedName("astro")
    public Astronomy astronomy;  //日出日落

    public Wind wind;  //风力情况


    public class Temperature {

        public String max;

        public String min;
    }

    public class Condition {

        public String code_n;

        public String code_d;

        @SerializedName("txt_d")
        public String dayConditon;

        @SerializedName("txt_n")
        public String nightCondition;
    }

    public class Astronomy {

        @SerializedName("mr")
        public String moonrise;

        @SerializedName("ms")
        public String moonset;

        @SerializedName("sr")
        public String sunrise;

        @SerializedName("ss")
        public String sunset;
    }

    public class Wind {

        @SerializedName("dir")
        public String direction;

        @SerializedName("sc")
        public String windFore;

        @SerializedName("spd")
        public String windSpeed;
    }
}
