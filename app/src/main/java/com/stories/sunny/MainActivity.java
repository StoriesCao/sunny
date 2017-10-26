package com.stories.sunny;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.stories.sunny.adapter.ViewPaperAdapter;
import com.stories.sunny.db_model.CityStoraged;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends BaseActivity{

    private static final String TAG = "MainActivity";

    public static final int REQUEST_CODE = 1;

    private int position; // 当前ViewPaper的位置

    private Toolbar mTitleToolBar;

    private static ViewPager mViewPager;

    private List<CityStoraged> cityStoragedList = DataSupport.findAll(CityStoraged.class);

    public static ViewPaperAdapter mViewPaperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (cityStoragedList.size() == 0) { //用户并没有添加任何城市
            Intent startCityManagerActivity = new Intent(MainActivity.this, CityManagerActivity.class);
            startActivity(startCityManagerActivity);
        }

        /* ****** */
        mTitleToolBar = (Toolbar) findViewById(R.id.title_main);
        setSupportActionBar(mTitleToolBar);


        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager = (ViewPager) findViewById(R.id.view_paper);
        mViewPaperAdapter = new ViewPaperAdapter(fragmentManager);
        mViewPager.setAdapter(mViewPaperAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTitleToolBar.setTitle(cityStoragedList.get(position).getCityStoragedName());
                Log.d(TAG, "onPageSelected: " + cityStoragedList.get(position).getCityStoragedName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
         });

        /* String position = getIntent().getStringExtra("position");
        if (position != null) {
            viewPager.setCurrentItem(Integer.parseInt(position));
        }*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        cityStoragedList = DataSupport.findAll(CityStoraged.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewPaperAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: " + requestCode + " " + resultCode);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    position = data.getIntExtra("position", 0);
                    Log.d(TAG, "onActivityResult: position" + position);
                    mViewPager.setCurrentItem(position, true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.weather_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.local_manager:
                startActivityForResult(new Intent(MainActivity.this, CityManagerActivity.class), 1);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        BaseActivity.finishAllActivity();
    }

}




















