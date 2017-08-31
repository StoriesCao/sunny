package com.stories.sunny.db_model;

import org.litepal.crud.DataSupport;

/**
 * Created by Charlottecao on 8/31/17.
 * The DB module of counties.
 */

public class County extends DataSupport {

    private int id;

    private int countyId;

    private int cityId;

    private String name;

    private String weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
