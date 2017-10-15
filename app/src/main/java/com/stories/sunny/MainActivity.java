package com.stories.sunny;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.stories.sunny.adapter.ViewPaperAdapter;
import com.stories.sunny.db_model.CityStoraged;

import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends BaseActivity {

    private ViewPager viewPager;

    private List<CityStoraged> cityStoragedList = DataSupport.findAll(CityStoraged.class);

    public static ViewPaperAdapter mViewPaperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        if (cityStoragedList.size() == 0) { //用户并没有添加任何城市
            Intent startCityManagerActivity = new Intent(MainActivity.this, CityManagerActivity.class);
            startActivity(startCityManagerActivity);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager = (ViewPager) findViewById(R.id.view_paper);
        mViewPaperAdapter = new ViewPaperAdapter(fragmentManager);
        viewPager.setAdapter(mViewPaperAdapter);

       /* String position = getIntent().getStringExtra("position");
        if (position != null) {
            viewPager.setCurrentItem(Integer.parseInt(position));
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewPaperAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        BaseActivity.finishAllActivity();
    }
}




















