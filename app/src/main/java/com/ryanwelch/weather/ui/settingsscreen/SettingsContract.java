/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.settingsscreen;

import com.ryanwelch.weather.ui.BasePresenter;

public class SettingsContract {

    public interface View {
        void setVersionSummary(String version);

        void setFlavorSummary(String flavor);
    }

    public interface Presenter extends BasePresenter<View> {

    }

}
