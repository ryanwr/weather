/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.ryanwelch.weather.data.db.CurrentWeatherTable;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import rx.Observable;
import timber.log.Timber;

public class WeatherLocalDataSource implements WeatherDataSource {

    private StorIOSQLite mStorIOSQLite;

    public WeatherLocalDataSource(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
    }

    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        Timber.d("LOCAL: getCurrentWeather(): %s", place.getName());
        return mStorIOSQLite
                .get()
                .object(CurrentWeather.class)
                .withQuery(
                        Query.builder()
                                .table(CurrentWeatherTable.TABLE)
                                .where(CurrentWeatherTable.COLUMN_LATITUDE + " = ? AND " + CurrentWeatherTable.COLUMN_LONGITUDE + " = ? ")
                                .whereArgs(place.getLatitude(), place.getLongitude())
                                .build()
                )
                .prepare()
                .asRxObservable()
                .take(1);
    }

    public Observable<Void> setCurrentWeather(CurrentWeather currentWeather) {
        Timber.d("LOCAL: setCurrentWeather(): %s", currentWeather.place.getName());
        return mStorIOSQLite
                .put()
                .object(currentWeather)
                .prepare()
                .asRxObservable()
                .take(1)
                .switchMap((res) -> Observable.empty());
    }

}
