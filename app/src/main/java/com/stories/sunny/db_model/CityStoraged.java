package com.stories.sunny.db_model;

import org.litepal.crud.DataSupport;

/**
 * Created by Charlottecao on 9/8/17.
 */

public class CityStoraged extends DataSupport {

    private int id;

    private String cityStoragedName;

    private int weatherImgId;

    private String maxMinDegree;

    public String getCityStoragedName() {
        return cityStoragedName;
    }

    public void setCityStoragedName(String cityStoragedName) {
        this.cityStoragedName = cityStoragedName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeatherImgId() {
        return weatherImgId;
    }

    public void setWeatherImgId(int weatherImgId) {
        this.weatherImgId = weatherImgId;
    }

    public String getMaxMinDegree() {
        return maxMinDegree;
    }

    public void setMaxMinDegree(String maxMinDegree) {
        this.maxMinDegree = maxMinDegree;
    }
}
