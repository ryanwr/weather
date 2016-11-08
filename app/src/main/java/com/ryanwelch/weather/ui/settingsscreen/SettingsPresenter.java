/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.settingsscreen;

import android.app.Application;
import android.content.SharedPreferences;

import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.R;

import com.ryanwelch.weather.injector.scopes.ActivityScope;

import javax.inject.Inject;

import timber.log.Timber;

@ActivityScope
public class SettingsPresenter implements SettingsContract.Presenter,
        SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject Application mApplication;
    @Inject SharedPreferences mSharedPreferences;

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
        mView.setVersionSummary(BuildConfig.VERSION_NAME);
        mView.setFlavorSummary(BuildConfig.FLAVOR);
    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Timber.i("Preference %s changed to %s.", key, sharedPreferences.getString(key, mApplication.getString(R.string.weather_preference_default)));
    }
}
