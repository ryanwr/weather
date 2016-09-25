/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import rx.Observable;

public class WeatherLocalDataSource implements WeatherDataSource {

    private StorIOSQLite storIOSQLite;

    public WeatherLocalDataSource(StorIOSQLite storIOSQLite) {

    }

    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        return null;
    }

    public Observable<Void> setCurrentWeather(CurrentWeather currentWeather) {
        return null;
    }

}
