package com.ryanwelch.weather.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.models.CurrentWeather;
import com.ryanwelch.weather.ui.helpers.ItemTouchHelperAdapter;
import com.ryanwelch.weather.ui.helpers.ItemTouchHelperViewHolder;
import com.ryanwelch.weather.ui.helpers.OnStartDragListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.WeatherItemViewHolder> implements ItemTouchHelperAdapter {

    @BindString(R.string.temperature_format) String mTemperatureFormat;

    private final List<CurrentWeather> mItems = new ArrayList<>();

    private final OnStartDragListener mDragStartListener;

    public WeatherListAdapter(Context context, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        mItems.add(new CurrentWeather("London", 27, 15, 28, "Overcast"));
    }



    @Override
    public WeatherItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_weather_card, parent, false);
        WeatherItemViewHolder itemViewHolder = new WeatherItemViewHolder(view);

        ButterKnife.bind(this, view);

        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final WeatherItemViewHolder holder, int position) {
        CurrentWeather data = mItems.get(position);

        Animation pulse = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.pulse);
        holder.mWeatherIconSunBg.startAnimation(pulse);

        Animation hover = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.hover);
        holder.mWeatherIconSun.startAnimation(hover);

        Animation hoverShadow = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.hover_shadow);
        holder.mWeatherIconShadow.startAnimation(hoverShadow);

        holder.mLocationName.setText(data.getCityName());
        holder.mTemperature.setText(String.format(mTemperatureFormat, data.getCurrentTemperature()));

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

        // TODO: Abstract icon
        @BindView(R.id.weather_icon_sun) RelativeLayout mWeatherIconSun;
        @BindView(R.id.weather_icon_sun_bg) ImageView mWeatherIconSunBg;
        @BindView(R.id.weather_icon_shadow) ImageView mWeatherIconShadow;


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
