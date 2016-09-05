package com.ryanwelch.weather.ui.mainscreen;

import android.util.Log;

import com.ryanwelch.weather.WeatherApplication;
import com.ryanwelch.weather.data.ResponseCallback;
import com.ryanwelch.weather.models.CurrentWeather;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    @Override
    public void setView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void addItem() {
        WeatherApplication.getWeatherProvider().getCurrentWeather(new ResponseCallback<CurrentWeather>() {
            @Override
            public void onSuccess(CurrentWeather data) {
                mWeatherListItems.add(data);
                mWeatherListAdapter.notifyDataSetChanged();
                Log.v("WeatherListAdapter", "Received data");
            }

            @Override
            public void onFailure(String error) {
                Log.e("WeatherListAdapter", error);
            }
        }, place);
    }

    @Override
    public void onRefresh() {

    }
}
