/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.ryanwelch.weather.data.db.CurrentWeatherTable;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import rx.Observable;

public class WeatherLocalDataSource implements WeatherDataSource {

    private StorIOSQLite mStorIOSQLite;

    public WeatherLocalDataSource(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
    }

    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        return mStorIOSQLite
                .get()
                .object(CurrentWeather.class)
                .withQuery(
                        Query.builder()
                                .table(CurrentWeatherTable.TABLE)
                                .where(CurrentWeatherTable.COLUMN_LATITUDE + " = ? AND " + CurrentWeatherTable.COLUMN_LONGITUDE + " = ? ")
                                .whereArgs(place.getLatitude(), place.getLatitude())
                                .build()
                )
                .prepare()
                .asRxSingle()
                .toObservable();
    }

    public Observable<Void> setCurrentWeather(CurrentWeather currentWeather) {
//        return mStorIOSQLite
//                .put()
//                .object(currentWeather)
//                .prepare()
//                .asRxSingle();
        return null;
    }

}
