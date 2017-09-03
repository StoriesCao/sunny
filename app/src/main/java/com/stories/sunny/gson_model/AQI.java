package com.stories.sunny.gson_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlottecao on 9/1/17.
 */

public class AQI {

    public AQICity city;

    public class AQICity {

        public String aqi;  //空气质量指数

        public String co;

        public String no2;

        public String o3;

        public String pm10;

        public String pm25;

        @SerializedName("qlty")
        public String airQulity;  //空气质量状况

        public String so2;
    }
}
