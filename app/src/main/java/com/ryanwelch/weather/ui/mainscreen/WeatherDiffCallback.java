package com.ryanwelch.weather.ui.mainscreen;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.ryanwelch.weather.domain.models.CurrentWeather;

import java.util.List;

public class WeatherDiffCallback extends DiffUtil.Callback {

    private final List<CurrentWeather> mOldList;
    private final List<CurrentWeather> mNewList;

    public WeatherDiffCallback(List<CurrentWeather> oldList, List<CurrentWeather> newList) {
        mOldList = oldList;
        mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).place.equals(mNewList.get(newItemPosition).place);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final CurrentWeather oldItem = mOldList.get(oldItemPosition);
        final CurrentWeather newItem = mNewList.get(newItemPosition);

        return oldItem.updateTime == newItem.updateTime
                && oldItem.weatherCondition == newItem.weatherCondition;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
