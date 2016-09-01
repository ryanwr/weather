package com.ryanwelch.weather.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.models.CurrentWeather;
import com.ryanwelch.weather.ui.helpers.ItemTouchHelperAdapter;
import com.ryanwelch.weather.ui.helpers.ItemTouchHelperViewHolder;
import com.ryanwelch.weather.ui.helpers.OnStartDragListener;
import com.ryanwelch.weather.ui.views.WeatherIconView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.WeatherItemViewHolder> implements ItemTouchHelperAdapter {

    String mTemperatureFormat;

    private final List<CurrentWeather> mItems;

    private final OnStartDragListener mDragStartListener;

    public WeatherListAdapter(Context context, ArrayList<CurrentWeather> items, OnStartDragListener dragStartListener) {
        mItems = items;
        mDragStartListener = dragStartListener;
        mTemperatureFormat = context.getResources().getString(R.string.temperature_format);
    }

    @Override
    public WeatherItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_weather_card, parent, false);
        WeatherItemViewHolder itemViewHolder = new WeatherItemViewHolder(view);

        return itemViewHolder;
    }

    private boolean isColorDark(int color){
        double darkness = 1-(0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        if(darkness<0.5){
            return false; // It's a light color
        }else{
            return true; // It's a dark color
        }
    }

    @Override
    public void onBindViewHolder(final WeatherItemViewHolder holder, int position) {
        CurrentWeather data = mItems.get(position);

        //int primaryTextColor = ContextCompat.getColor(holder.mCardView.getContext(), R.color);
        //if(isColorDark(data.weatherCondition.getIcon().getColor())) {
        //
        //}

        holder.mCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                        holder.mCardView.getContext(),
                        data.weatherCondition.getIcon().getColor()));

        holder.mDate.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(data.updateTime));
        //holder.mDate.setTextColor();
        holder.mLocationName.setText(data.cityName);
        holder.mTemperature.setText(String.format(mTemperatureFormat, (long) Math.round(data.temperature)));
        holder.mWeatherIcon.createIcon(data.weatherCondition.getIcon());

        // Start a drag whenever the handle view it touched
//        holder.mCardView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN ||
//                        MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_MOVE) {
//                    mDragStartListener.onStartDrag(holder);
//                }
//                return false;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public static class WeatherItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        @BindView(R.id.card_view) CardView mCardView;
        @BindView(R.id.txt_location) TextView mLocationName;
        @BindView(R.id.txt_date_time) TextView mDate;
        @BindView(R.id.txt_temperature) TextView mTemperature;
        @BindView(R.id.txt_temperature_hilo) TextView mTemperatureHiLo;
        @BindView(R.id.weather_icon) WeatherIconView mWeatherIcon;

        public WeatherItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

}
