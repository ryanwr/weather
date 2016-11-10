/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.ui.BasePresenter;

public class DetailContract {

    public interface View {
        void showLoading();

        void hideLoading();

        void showData(Weather weather);
    }

    public interface Presenter extends BasePresenter<View> {
        void setData(Weather weather);

        Weather getData();
    }

}
