/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather.darksky;


import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public class DarkSkyDataSource implements WeatherRemoteDataSource {

    public static final String DARK_SKY_BASE_URL = "https://api.darksky.net/";
    public static final String DARK_SKY_API_KEY = BuildConfig.DARK_SKY_API_TOKEN;

    private WeatherService mWeatherService;

    public DarkSkyDataSource(Retrofit retrofit) {
        mWeatherService = retrofit.create(WeatherService.class);
    }

    @Override
    public Observable<Weather> getCurrentWeather(Place place) {
        return mWeatherService.getCurrentWeather(String.valueOf(place.getLatitude()),
                String.valueOf(place.getLongitude()))
                .map(this::transform);
    }

    private Weather transform(DarkSkyWeatherResponse weatherResponse) {
        if(weatherResponse == null) return null;

        Weather weather = new Weather();
//        weather.place = weatherResponse.place;
//        //weather.updateTime = new Date(weatherResponse.current.lastUpdatedEpoch * 1000);
//        weather.updateTime = new Date();
//        weather.temperatureC = weatherResponse.current.tempC;
//        weather.feelsLikeC = weatherResponse.current.feelsLikeC;
//        weather.pressure = weatherResponse.current.pressureMb;
//        weather.humidity = weatherResponse.current.humidity;
//        weather.windSpeed = weatherResponse.current.windKph;
//        weather.windDegree = weatherResponse.current.windDegree;
//        weather.windDirection = weatherResponse.current.windDir;
//        weather.isDay = weatherResponse.current.isDay != 0;
//        weather.precipitation = weatherResponse.current.precipMm;
//        weather.snow = 0;
//        weather.cloud = weatherResponse.current.cloud;
//        weather.sunriseTime = new Date();
//        weather.sunsetTime = new Date();
//
//        switch (weatherResponse.current.condition.code) {
//
//            default:
//                weather.weatherCondition = WeatherCondition.SUNNY;
//        }

        return weather;
    }

    interface WeatherService {
        @GET("forecast/" + DARK_SKY_API_KEY + "/{latitude},{longitude}")
        Observable<DarkSkyWeatherResponse> getCurrentWeather(@Path("latitude") String latitude,
                                                             @Path("longitude") String longitude);
    }

}
