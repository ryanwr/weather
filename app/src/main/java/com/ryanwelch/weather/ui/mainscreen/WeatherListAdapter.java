package com.ryanwelch.weather.ui.mainscreen;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.ui.helpers.ItemTouchHelperAdapter;
import com.ryanwelch.weather.ui.helpers.ItemTouchHelperViewHolder;
import com.ryanwelch.weather.ui.components.WeatherIconView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherListAdapter extends RecyclerView.Adapter<WeatherListAdapter.WeatherItemViewHolder> implements ItemTouchHelperAdapter {

    private String mTemperatureFormat;
    private String mFeelsLikeFormat;
    private List<CurrentWeather> mItems = new ArrayList<>();
    private Callback mCallback;

    public WeatherListAdapter(Context context, ArrayList<CurrentWeather> items, Callback callback) {
        replaceData(items);
        mTemperatureFormat = context.getResources().getString(R.string.temperature_format);
        mFeelsLikeFormat = context.getResources().getString(R.string.feels_like_format);
        mCallback = callback;
    }

    @Override
    public WeatherItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_weather_card, parent, false);
        return new WeatherItemViewHolder(view);
    }

    private boolean isColorDark(int color){
        double darkness = 1 - (0.299* Color.red(color) + 0.587*Color.green(color) + 0.114*Color.blue(color))/255;
        return darkness >= 0.5;
    }

    @Override
    public void onBindViewHolder(final WeatherItemViewHolder holder, int position) {
        CurrentWeather data = mItems.get(position);

        holder.mCardView.setCardBackgroundColor(
                ContextCompat.getColor(
                        holder.mCardView.getContext(),
                        data.weatherCondition.getIcon().getColor()));

        //holder.mDate.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(data.updateTime));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.setTransitionId(Integer.toString(position));
            holder.itemView.setTransitionName("weather_item_" + holder.getTransitionId());
            holder.mWeatherIcon.setTransitionName("weather_icon_" + holder.getTransitionId());
            holder.mLocationName.setTransitionName("weather_name_" + holder.getTransitionId());
            holder.mTemperature.setTransitionName("weather_temperature_" + holder.getTransitionId());
            holder.mCondition.setTransitionName("weather_condition_" + holder.getTransitionId());
        }
        holder.mCondition.setText(data.isDay ? data.weatherCondition.getName() : data.weatherCondition.getNightName());
//        holder.mFeelsLike.setText(String.format(mFeelsLikeFormat, (long) Math.round(data.feelsLikeC)));
        holder.mLocationName.setText(data.place.getName());
        holder.mTemperature.setText(String.format(mTemperatureFormat, (long) Math.round(data.temperatureC)));
        holder.mWeatherIcon.setIcon(data.isDay ? data.weatherCondition.getIcon() : data.weatherCondition.getNightIcon());
    }

    public void replaceData(List<CurrentWeather> items) {
        final WeatherDiffCallback diffCallback = new WeatherDiffCallback(mItems, items);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        mItems.clear();
        mItems.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    public CurrentWeather getItemAt(int position) {
        return mItems.get(position);
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
        mCallback.onItemDismiss(mItems.remove(position));
        notifyItemRemoved(position);
    }

    public static class WeatherItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        @BindView(R.id.card_view) public CardView mCardView;
        @BindView(R.id.txt_location) public TextView mLocationName;
        @BindView(R.id.txt_condition) public TextView mCondition;
        @BindView(R.id.txt_temperature) public TextView mTemperature;
//        @BindView(R.id.txt_feels_like) public TextView mFeelsLike;
        @BindView(R.id.weather_icon) public WeatherIconView mWeatherIcon;

        private String mTransitionId;

        public WeatherItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setTransitionId(String transitionId) {
            mTransitionId = transitionId;
        }

        public String getTransitionId() {
            return mTransitionId;
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

    public interface Callback {
        void onItemDismiss(CurrentWeather weather);
    }

}
