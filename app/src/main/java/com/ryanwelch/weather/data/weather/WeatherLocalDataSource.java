/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import rx.Observable;

public class WeatherLocalDataSource implements WeatherDataSource {



    @Override
    public Observable<CurrentWeather> currentWeather(Place place) {
        return null;
    }

}
