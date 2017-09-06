package com.stories.sunny.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.stories.sunny.CityStoraged;
import com.stories.sunny.R;

import java.util.List;

/**
 * Created by Charlottecao on 9/6/17.
 */

public class CityStoragedAdapter extends ArrayAdapter {

    private int resourceId;

    public CityStoragedAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CityStoraged> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        CityStoraged cityStoraged = (CityStoraged) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        ImageView cityStoragedIcon = (ImageView) view.findViewById(R.id.city_storaged_icon);
        TextView cityStoragedName = (TextView) view.findViewById(R.id.city_storaged_name);
        TextView cityStoragedTemp = (TextView) view.findViewById(R.id.city_storaged_temp);

        cityStoragedIcon.setImageResource(cityStoraged.getWeatherImageId());
        cityStoragedName.setText(cityStoraged.getCityName());
        cityStoragedTemp.setText(cityStoraged.getCuurentDegree());

        return view;
    }
}
