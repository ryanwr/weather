/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.settingsscreen;

import android.content.SharedPreferences;

import com.ryanwelch.weather.BuildConfig;

import javax.inject.Inject;

import timber.log.Timber;

public class SettingsPresenter implements SettingsContract.Presenter,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private SettingsContract.View mView;

    @Inject
    public SettingsPresenter() {}

    @Override
    public void setView(SettingsContract.View view) {
        mView = view;
    }

    @Override
    public void resume() {
        initialize();
    }

    private void initialize() {
        mView.setVersion(BuildConfig.VERSION_NAME);
        mView.setFlavor(BuildConfig.FLAVOR);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Timber.i("Preference %s changed.", key);
    }
}
