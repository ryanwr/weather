/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.WeatherData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastListAdapter extends RecyclerView.Adapter<ForecastListAdapter.ForecastViewHolder>  {

    private List<WeatherData> mItems = new ArrayList<>();

    private String mTemperatureFormat;
    private SimpleDateFormat mDayFormat = new SimpleDateFormat("dd");

    public ForecastListAdapter(Context context, ArrayList<WeatherData> items) {
        mTemperatureFormat = context.getResources().getString(R.string.temperature_format);
        replaceData(items);
    }

    @Override
    public ForecastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_forecast_item, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastViewHolder holder, int position) {
        WeatherData data = mItems.get(position);

        holder.mForecastDay.setText(mDayFormat.format(data.time));
        // TODO
        //holder.mIcon
        holder.mForecastTemperature.setText(String.format(mTemperatureFormat, (long) Math.round(data.temperatureC)));
    }

    public void replaceData(List<WeatherData> items) {
        mItems.clear();
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ForecastViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_forecast_day) TextView mForecastDay;
        @BindView(R.id.forecast_icon) ImageView mIcon;
        @BindView(R.id.txt_forecast_temp) TextView mForecastTemperature;

        public ForecastViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
