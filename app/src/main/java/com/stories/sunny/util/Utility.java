package com.stories.sunny.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.stories.sunny.db_model.City;
import com.stories.sunny.db_model.County;
import com.stories.sunny.db_model.Province;
import com.stories.sunny.gson_model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Charlottecao on 8/31/17.
 */

public class Utility {

    /**
     * Parse provinces JSON and put the data in DB.
     * @param response the response json .
     * @return if handle and parse JSON correctly.
     */
    public static boolean parseProvinceJson(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Province province = new Province();
                    province.setName(jsonObject.getString("name"));
                    province.setProvinceId(jsonObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Parse cities JSON and put the date in DB.
     * @param response the response json .
     * @param provinceId the province id that the city belong to.
     * @return if handle and parse JSON correctly.
     */
    public static boolean parseCityJson(String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    City city = new City();
                    city.setCityId(jsonObject.getInt("id"));
                    city.setName(jsonObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Parse countoes JSON and put the date in DB.
     * @param response the response json.
     * @param cityId the city id that the county belong to.
     * @return if handle and parse JSON correctly.
     */
    public static boolean parseCountyJson(String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    County county = new County();
                    county.setCountyId(jsonObject.getInt("id"));
                    county.setName(jsonObject.getString("name"));
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.setCityId(cityId);
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * Parse the main JSON about weather.
     * @param response the response json.
     * @return the Weather Object.
     */
    public static Weather parseWeatherJson(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}




































