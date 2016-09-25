package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import java.util.Date;

import rx.Observable;

public class WeatherRepository implements WeatherDataSource {

    // 10 Minutes expire time
    public static final long EXPIRE_TIME = 10 * 60 * 1000;

    private WeatherRemoteDataSource mWeatherRemoteDataSource;
    private WeatherLocalDataSource mWeatherLocalDataSource;

    public WeatherRepository(WeatherRemoteDataSource weatherRemoteDataSource,
                             WeatherLocalDataSource weatherLocalDataSource) {
        mWeatherRemoteDataSource = weatherRemoteDataSource;
        mWeatherLocalDataSource = weatherLocalDataSource;
    }

    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        // TODO: Check if updateTime is accurate, change it to time since fetched from remote?
        return mWeatherLocalDataSource.getCurrentWeather(place)
                .filter(v -> new Date().before(new Date(v.updateTime.getTime() + EXPIRE_TIME)))
                .switchIfEmpty(
                        mWeatherRemoteDataSource.getCurrentWeather(place)
                                .doOnNext(mWeatherLocalDataSource::setCurrentWeather)
                );
    }
}
