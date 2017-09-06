package com.stories.sunny;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.stories.sunny.adapter.CityStoragedAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charlottecao on 9/5/17.
 */

public class ChooseAreaActivity extends AppCompatActivity {

    private List<CityStoraged> cityStoragedDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);

        init();

        ListView cityStoragedList = (ListView) findViewById(R.id.add_city_list);
        CityStoragedAdapter cityStoragedAdapter = new CityStoragedAdapter(ChooseAreaActivity.this, R.layout.city_storaged, cityStoragedDataList);
        cityStoragedList.setAdapter(cityStoragedAdapter);


    }

    private void init() {

        for (int i = 1; i <= 3; i++) {
            CityStoraged cityStoraged1 = new CityStoraged("北京", "8 ~ 15 °C", R.drawable.ic_heavy_rain);
            cityStoragedDataList.add(cityStoraged1);
            CityStoraged cityStoraged2 = new CityStoraged("北京", "8 ~ 15 °C", R.drawable.ic_heavy_rain);
            cityStoragedDataList.add(cityStoraged2);
            CityStoraged cityStoraged3 = new CityStoraged("北京", "8 ~ 15 °C", R.drawable.ic_heavy_rain);
            cityStoragedDataList.add(cityStoraged3);
        }
    }
}
