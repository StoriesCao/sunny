package com.stories.sunny;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.stories.sunny.adapter.CityStoragedAdapter;
import com.stories.sunny.adapter.ViewPaperAdapter;
import com.stories.sunny.db_model.CityStoraged;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Charlottecao on 9/5/17.
 */

public class CityManagerActivity extends BaseActivity {

    private List<CityStoraged> cityStoragedList;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_city);

        FragmentManager fm = getSupportFragmentManager();
        final ViewPaperAdapter adapter = new ViewPaperAdapter(fm);

        cityStoragedList = DataSupport.findAll(CityStoraged.class);

        ListView cityStoragedListView = (ListView) findViewById(R.id.add_city_list);
        final CityStoragedAdapter cityStoragedAdapter = new CityStoragedAdapter(CityManagerActivity.this, R.layout.city_storaged, cityStoragedList);
        cityStoragedListView.setAdapter(cityStoragedAdapter);
        cityStoragedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(CityManagerActivity.this, cityStoragedList.get(i).getCityStoragedName() + "短", Toast.LENGTH_SHORT).show();
            }
        });
        cityStoragedListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CityManagerActivity.this);
                dialog.setTitle("警告：");
                dialog.setMessage("确定要删除 " + cityStoragedList.get(position).getCityStoragedName() + " 吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataSupport.deleteAll(CityStoraged.class, "citystoragedname = ?", cityStoragedList.get(position).getCityStoragedName());
                        cityStoragedAdapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();

                return true;
            }
        });

        /* ****** */
        toolbar = (Toolbar) findViewById(R.id.city_manager_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_button_light);
        }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
            default:
                break;
        }
        return true;
    }

}
