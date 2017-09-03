package com.stories.sunny.gson_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlottecao on 9/1/17.
 */

public class Basic {

    @SerializedName("city")
    public String cityName;   //城市名称

    @SerializedName("id")
    public String weatherId;  //城市id

    public Update update;     //更新时间

    public class Update {

        @SerializedName("loc")
        public String updateTime;
    }
}
