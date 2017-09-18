package com.stories.sunny;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;

import com.stories.sunny.adapter.CityStoragedAdapter;
import com.stories.sunny.db_model.CityStoraged;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Charlottecao on 9/5/17.
 */

public class CityManagerActivity extends BaseActivity {

    private static final String TAG = "CityManagerActivity";

    private List<CityStoraged> cityStoragedList = new ArrayList<>();

    private Toolbar toolbar;

    private FloatingActionButton mAddCityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_city);

        cityStoragedList = DataSupport.findAll(CityStoraged.class);

        final ListView cityStoragedListView = (ListView) findViewById(R.id.add_city_list);
        final CityStoragedAdapter cityStoragedAdapter = new CityStoragedAdapter(CityManagerActivity.this, R.layout.city_storaged, cityStoragedList);
        cityStoragedListView.setAdapter(cityStoragedAdapter);
        cityStoragedListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CityManagerActivity.this, MainActivity.class);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });
        cityStoragedListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() { // 长按删除存储的城市
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(CityManagerActivity.this);
                dialog.setTitle("警告：");
                dialog.setMessage("确定要删除 " + cityStoragedList.get(position).getCityStoragedName() + " 吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //先删除相应的JSON数据
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CityManagerActivity.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        if (preferences.getString(cityStoragedList.get(position).getCityStoragedName() + "weatherJSON", null) != null) {
                            /*
                            无法删除？？
                            editor.remove(cityStoragedList.get(position).getCityStoragedName() + "weatherJSON");
                             */
                            editor.putString(cityStoragedList.get(position).getCityStoragedName() + "weatherJSON", null);
                        }
                        DataSupport.deleteAll(CityStoraged.class, "citystoragedname = ?", cityStoragedList.get(position).getCityStoragedName());
                        cityStoragedList.remove(position); //仅仅从数据库删除了此条数据，List集合里面并没有删除，size - 1

                        /* 为什么此种方法删除List集合不成功，全部删除？
                        cityStoragedList.clear();
                        cityStoragedList = DataSupport.findAll(CityStoraged.class);*/

                        cityStoragedAdapter.notifyDataSetChanged();

                        MainActivity.mViewPaperAdapter.notifyDataSetChanged();
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
        mAddCityButton = (FloatingActionButton) findViewById(R.id.add_city);
        mAddCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(CityManagerActivity.this, R.anim.anim_rotate_float_button);
                mAddCityButton.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(CityManagerActivity.this, ChooseAreaActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_float_button);
        animation.setStartOffset(500);
        mAddCityButton.startAnimation(animation);
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
