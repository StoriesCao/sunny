package com.stories.sunny;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.stories.sunny.adapter.CityStoragedAdapter;
import com.stories.sunny.db_model.CityStoraged;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Charlottecao on 9/5/17.
 */

public class CityManagerActivity extends AppCompatActivity {

    private List<CityStoraged> cityStoragedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_city);

        cityStoragedList = DataSupport.findAll(CityStoraged.class);

        ListView cityStoragedListView = (ListView) findViewById(R.id.add_city_list);
        CityStoragedAdapter cityStoragedAdapter = new CityStoragedAdapter(CityManagerActivity.this, R.layout.city_storaged, cityStoragedList);
        cityStoragedListView.setAdapter(cityStoragedAdapter);


        /* ****** */
        FloatingActionButton addCityButton = (FloatingActionButton) findViewById(R.id.add_city);
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CityManagerActivity.this, ChooseAreaActivity.class);
                startActivity(intent);
            }
        });


    }

}
