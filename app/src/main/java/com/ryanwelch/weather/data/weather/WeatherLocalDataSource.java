/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.ryanwelch.weather.data.db.CurrentWeatherTable;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;

import rx.Observable;
import timber.log.Timber;

public class WeatherLocalDataSource implements WeatherDataSource {

    private StorIOSQLite mStorIOSQLite;

    public WeatherLocalDataSource(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
    }

    @Override
    public Observable<Weather> getCurrentWeather(Place place) {
        Timber.d("LOCAL: getCurrentWeather(): %s", place.getName());
        return mStorIOSQLite
                .get()
                .object(Weather.class)
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

    public Observable<Void> setCurrentWeather(Weather weather) {
        Timber.d("LOCAL: setCurrentWeather(): %s", weather.place.getName());
        return mStorIOSQLite
                .put()
                .object(weather)
                .prepare()
                .asRxObservable()
                .take(1)
                .switchMap((res) -> Observable.empty());
    }

}
