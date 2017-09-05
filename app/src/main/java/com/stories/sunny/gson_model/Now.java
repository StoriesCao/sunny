package com.stories.sunny.gson_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlottecao on 9/1/17.
 */

public class Now {

    @SerializedName("fl")
    public String realFeel;  //体感温度

    @SerializedName("hum")
    public String relativeHumidity;  //相对湿度

    @SerializedName("pcpn")
    public String precipitation;   //降水量

    @SerializedName("pres")
    public String atmosphericPressure;  //大气压

    @SerializedName("vis")
    public String visibility;   //能见度

    @SerializedName("tmp")
    public String Temperature;  //温度

    @SerializedName("cond")
    public Condition condition;  //基本天气信息

    public Wind wind;  //风力情况


    public class Condition {

        @SerializedName("txt")
        public String weatherInfo;
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
