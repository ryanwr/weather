/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data;

import com.ryanwelch.weather.data.weather.WeatherDataSource;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import rx.Observable;

public class MockWeatherRepository implements WeatherDataSource {
    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        return null;
    }
}
