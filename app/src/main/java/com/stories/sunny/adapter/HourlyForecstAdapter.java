package com.stories.sunny.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stories.sunny.R;
import com.stories.sunny.gson_model.HourlyForecast;
import com.stories.sunny.util.IconAndColorUtils;

import java.util.List;

/**
 * Created by Charlottecao on 11/3/17.
 */

public class HourlyForecstAdapter extends RecyclerView.Adapter<HourlyForecstAdapter.ViewHolder>{

    private List<HourlyForecast> mHourlyForecastsList;

//    设置数据源
    public HourlyForecstAdapter(List<HourlyForecast> hourlyForecastsList) {
        mHourlyForecastsList = hourlyForecastsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView mImageView;
        TextView mWind;
        TextView mDegree;
        TextView mTime;
        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.hourly_forecast_item_card_view);
            mImageView = (ImageView) itemView.findViewById(R.id.hourly_forecast_item_icon);
            mWind = (TextView) itemView.findViewById(R.id.hourly_forecast_item_wind);
            mDegree = (TextView) itemView.findViewById(R.id.hourly_forecast_item_temperature);
            mTime = (TextView) itemView.findViewById(R.id.hourly_forecast_item_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HourlyForecast hourlyForecast = mHourlyForecastsList.get(position);
        holder.mCardView.setCardBackgroundColor(IconAndColorUtils.getColor(Integer.parseInt(hourlyForecast.condition.code)));
        holder.mDegree.setText(hourlyForecast.temperature + "°");
        holder.mTime.setText(hourlyForecast.date.split(" ")[1]);
        holder.mWind.setText(hourlyForecast.wind.direction);
        holder.mImageView.setImageResource(IconAndColorUtils.getIcon(Integer.parseInt(hourlyForecast.condition.code)));
    }


    @Override
    public int getItemCount() {
        return mHourlyForecastsList.size();
    }
}
