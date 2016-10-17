/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather.darksky;


import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;

import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public class DarkSkyDataSource implements WeatherRemoteDataSource {

    private static final String DARK_SKY_API_KEY = BuildConfig.DARK_SKY_API_TOKEN;

    private WeatherService mWeatherService;

    public DarkSkyDataSource(Retrofit retrofit) {
        mWeatherService = retrofit.create(WeatherService.class);
    }

    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        return mWeatherService.getCurrentWeather(String.valueOf(place.getLatitude()),
                String.valueOf(place.getLongitude()))
                .map(this::transform);
    }

    private CurrentWeather transform(DarkSkyWeatherResponse weatherResponse) {
        if(weatherResponse == null) return null;

        CurrentWeather currentWeather = new CurrentWeather();
//        currentWeather.place = weatherResponse.place;
//        //currentWeather.updateTime = new Date(weatherResponse.current.lastUpdatedEpoch * 1000);
//        currentWeather.updateTime = new Date();
//        currentWeather.temperatureC = weatherResponse.current.tempC;
//        currentWeather.feelsLikeC = weatherResponse.current.feelsLikeC;
//        currentWeather.pressure = weatherResponse.current.pressureMb;
//        currentWeather.humidity = weatherResponse.current.humidity;
//        currentWeather.windSpeed = weatherResponse.current.windKph;
//        currentWeather.windDegree = weatherResponse.current.windDegree;
//        currentWeather.windDirection = weatherResponse.current.windDir;
//        currentWeather.isDay = weatherResponse.current.isDay != 0;
//        currentWeather.precipitation = weatherResponse.current.precipMm;
//        currentWeather.snow = 0;
//        currentWeather.cloud = weatherResponse.current.cloud;
//        currentWeather.sunriseTime = new Date();
//        currentWeather.sunsetTime = new Date();
//
//        switch (weatherResponse.current.condition.code) {
//
//            default:
//                currentWeather.weatherCondition = WeatherCondition.SUNNY;
//        }

        return currentWeather;
    }

    interface WeatherService {
        @GET("forecast/" + DARK_SKY_API_KEY +"/{latitude},{longitude}")
        Observable<DarkSkyWeatherResponse> getCurrentWeather(@Path("latitude") String latitude,
                                                             @Path("longitude") String longitude);
    }

}
