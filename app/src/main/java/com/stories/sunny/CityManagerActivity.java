package com.stories.sunny;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.stories.sunny.adapter.CityStoragedAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charlottecao on 9/5/17.
 */

public class CityManagerActivity extends AppCompatActivity {

    private List<CityStoraged> cityStoragedDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_city);

        init();

        ListView cityStoragedList = (ListView) findViewById(R.id.add_city_list);
        CityStoragedAdapter cityStoragedAdapter = new CityStoragedAdapter(CityManagerActivity.this, R.layout.city_storaged, cityStoragedDataList);
        cityStoragedList.setAdapter(cityStoragedAdapter);


        /* ****** */
        Button addCityButton = (Button) findViewById(R.id.add_city);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CityManagerActivity.this, ChooseAreaActivity.class);
                startActivity(intent);
            }
        });


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
