/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.WeatherCondition;
import com.ryanwelch.weather.domain.models.WeatherData;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

public class DetailForecastPresenter implements DetailForecastContract.Presenter {

    private DetailForecastContract.View mView;
    private Weather mWeather;

    @Inject
    public DetailForecastPresenter() {}

    @Override
    public void setData(Weather weather) {
        mWeather = weather;
    }

    @Override
    public void setView(DetailForecastContract.View view) {
        mView = view;
    }

    @Override
    public void resume() {
        ArrayList<WeatherData> data = new ArrayList<>();
        data.add(new WeatherData(WeatherCondition.MOSTLY_SUNNY, 10.5, new Date()));
        data.add(new WeatherData(WeatherCondition.THUNDERSTORMS, 9.5, new Date()));
        mView.showData(data);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
