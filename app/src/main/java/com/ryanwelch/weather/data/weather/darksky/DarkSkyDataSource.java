/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather.darksky;


import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import retrofit2.Retrofit;
import rx.Observable;

public class DarkSkyDataSource implements WeatherRemoteDataSource {

    private WeatherService mWeatherService;

    public DarkSkyDataSource(Retrofit retrofit) {
        mWeatherService = retrofit.create(WeatherService.class);
    }

    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        return null;
    }

    interface WeatherService {
        //@GET("current.json")
        //Observable<ApixuWeatherResponse> getCurrentWeather(@Query("q") String query);
    }

}
