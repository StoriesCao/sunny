package com.stories.sunny;

/**
 * Created by Charlottecao on 9/6/17.
 */

public class CityStoraged {

    private String cityName;

    private String cuurentDegree;

    private int weatherImageId;

    public CityStoraged(String cityName, String cuurentDegree, int weatherImageId) {
        this.cityName = cityName;
        this.cuurentDegree = cuurentDegree;
        this.weatherImageId = weatherImageId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCuurentDegree() {
        return cuurentDegree;
    }

    public int getWeatherImageId() {
        return weatherImageId;
    }

}
