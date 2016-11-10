/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;


import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.WeatherData;
import com.ryanwelch.weather.ui.BasePresenter;

import java.util.List;

public class DetailForecastContract {

    public interface View {
        void showData(List<WeatherData> data);
    }

    public interface Presenter extends BasePresenter<DetailForecastContract.View> {
        void setData(Weather weather);
    }

}
