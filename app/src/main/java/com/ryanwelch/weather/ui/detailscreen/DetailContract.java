/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.ui.BasePresenter;

public class DetailContract {

    public interface View {
        void showLoading();

        void hideLoading();
    }

    public interface Presenter extends BasePresenter<View> {
        void loadData();

        void setPlace(Place place);
    }

}
