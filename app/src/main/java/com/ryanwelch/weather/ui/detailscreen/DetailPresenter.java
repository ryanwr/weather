/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import android.util.Log;

import com.ryanwelch.weather.domain.models.Place;

import javax.inject.Inject;

public class DetailPresenter implements DetailContract.Presenter {

    private static final String TAG = "DetailPresenter";

    private DetailContract.View mView;
    private Place mPlace;

    @Inject
    public DetailPresenter() {}

    @Override
    public void setView(DetailContract.View view) {
        mView = view;
    }

    @Override
    public void setPlace(Place place) {
        mPlace = place;
    }

    @Override
    public void loadData() {
        Log.v(TAG, "Load data " + mPlace.getName());
    }

    @Override
    public void resume() {
        loadData();
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
