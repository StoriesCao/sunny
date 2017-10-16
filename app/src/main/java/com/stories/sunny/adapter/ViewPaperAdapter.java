package com.stories.sunny.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.stories.sunny.WeatherFragment;
import com.stories.sunny.db_model.CityStoraged;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Charlottecao on 9/8/17.
 */

public class ViewPaperAdapter extends FragmentStatePagerAdapter{

    private List<CityStoraged> cityStoragedList;

    public ViewPaperAdapter(FragmentManager fm) {
        super(fm);
        cityStoragedList = DataSupport.findAll(CityStoraged.class);
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragment.newInstance(cityStoragedList.get(position).getWeatherId(), cityStoragedList.get(position).getCityStoragedName());
    }

    @Override
    public int getCount() {
        return cityStoragedList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void updateList() {
        cityStoragedList = DataSupport.findAll(CityStoraged.class);
    }

}
