package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import javax.inject.Inject;

import rx.Observable;

public class WeatherRepository implements WeatherDataSource {

    private WeatherRemoteDataSource mWeatherRemoteDataSource;

    @Inject
    public WeatherRepository(WeatherRemoteDataSource weatherRemoteDataSource) {
        mWeatherRemoteDataSource = weatherRemoteDataSource;
    }

    @Override
    public Observable<CurrentWeather> currentWeather(Place place) {
        return mWeatherRemoteDataSource.currentWeather(place);
    }
}
