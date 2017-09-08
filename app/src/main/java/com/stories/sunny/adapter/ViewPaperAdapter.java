package com.stories.sunny.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.stories.sunny.WeatherFragment;
import com.stories.sunny.db_model.CityStoraged;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Charlottecao on 9/8/17.
 */

public class ViewPaperAdapter extends FragmentPagerAdapter {

    private List<CityStoraged> cityStoragedList = DataSupport.findAll(CityStoraged.class);

    public ViewPaperAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return WeatherFragment.newInstance(cityStoragedList.get(position).getWeatherId());
    }

    @Override
    public int getCount() {
        return cityStoragedList.size();
    }
}
