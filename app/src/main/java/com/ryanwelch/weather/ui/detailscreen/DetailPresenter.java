/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import android.util.Log;

import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import javax.inject.Inject;

public class DetailPresenter implements DetailContract.Presenter {

    private static final String TAG = "DetailPresenter";

    private DetailContract.View mView;
    private CurrentWeather mWeather;

    @Inject
    public DetailPresenter() {}

    @Override
    public void setView(DetailContract.View view) {
        mView = view;
    }

    @Override
    public void setData(CurrentWeather weather) {
        mWeather = weather;
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
