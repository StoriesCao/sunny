package com.stories.sunny.gson_model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Charlottecao on 9/1/17.
 */

public class Suggestion {

    public AirCondition air;

    @SerializedName("comf")
    public Comf comfort;  //舒适指数

    @SerializedName("cw")
    public CarWash carWash;  //洗车指数

    @SerializedName("drsg")
    public Dress dress;   //穿衣指数

    public Flu flu;  //感冒指数

    public Sport sport;  //运动指数

    @SerializedName("trav")
    public Travel travel;   //旅行指数

    @SerializedName("uv")
    public UltravioletRay ultravioletRay; //紫外线指数

    public class Comf {

        @SerializedName("brf")
        public String briefInfo;  //简介

        @SerializedName("txt")    //详细信息
        public String info;
    }

    public class CarWash {

        @SerializedName("brf")
        public String briefInfo;

        @SerializedName("txt")
        public String info;
    }

    public class Dress {

        @SerializedName("brf")
        public String briefInfo;

        @SerializedName("txt")
        public String info;
    }

    public class Flu {

        @SerializedName("brf")
        public String briefInfo;

        @SerializedName("txt")
        public String info;
    }

    public class Sport {

        @SerializedName("brf")
        public String briefInfo;

        @SerializedName("txt")
        public String info;
    }

    public class Travel {

        @SerializedName("brf")
        public String briefInfo;

        @SerializedName("txt")
        public String info;
    }

    public class UltravioletRay {

        @SerializedName("brf")
        public String briefInfo;

        @SerializedName("txt")
        public String info;
    }

    public class AirCondition {

        @SerializedName("brf")
        public String brifInfo;

        @SerializedName("txt")
        public String info;
    }
}
