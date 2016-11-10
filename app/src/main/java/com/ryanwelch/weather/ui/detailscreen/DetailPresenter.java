/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import com.ryanwelch.weather.domain.models.Weather;

import javax.inject.Inject;

public class DetailPresenter implements DetailContract.Presenter {

    private DetailContract.View mView;
    private Weather mWeather;

    @Inject
    public DetailPresenter() {}

    @Override
    public void setView(DetailContract.View view) {
        mView = view;
    }

    @Override
    public void setData(Weather weather) {
        mWeather = weather;
    }

    @Override
    public Weather getData() {
        return mWeather;
    }

    @Override
    public void resume() {
        mView.showData(mWeather);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
