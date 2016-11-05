/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.settingsscreen;

import javax.inject.Inject;

public class SettingsPresenter implements SettingsContract.Presenter {

    private SettingsContract.View mView;

    @Inject
    public SettingsPresenter() {}

    @Override
    public void setView(SettingsContract.View view) {
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

}
