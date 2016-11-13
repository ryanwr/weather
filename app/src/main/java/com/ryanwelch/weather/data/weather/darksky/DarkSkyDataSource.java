/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather.darksky;


import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.data.weather.apixu.ApixuForecastResponse;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;
import com.ryanwelch.weather.domain.models.WeatherData;

import java.util.ArrayList;
import java.util.Date;

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
    public Observable<Weather> getWeather(Place place) {
        return mWeatherService.getWeather(String.valueOf(place.getLatitude()),
                String.valueOf(place.getLongitude()))
                .map((weatherResponse -> transform(weatherResponse, place)));
    }

    private Weather transform(DarkSkyWeatherResponse weatherResponse, Place place) {
        if(weatherResponse == null) return null;

        Weather weather = new Weather();
        weather.place = place;
        weather.updateTime = new Date();
        weather.temperatureC = weatherResponse.currently.temperature;
        weather.feelsLikeC = weatherResponse.currently.apparentTemperature;
        weather.pressure = weatherResponse.currently.pressure;
        weather.humidity = weatherResponse.currently.humidity;
        weather.windSpeed = weatherResponse.currently.windSpeed;
        weather.windDegree = weatherResponse.currently.windBearing;
        //weather.windDirection = weatherResponse.current.windDir;
        //weather.isDay = weatherResponse.current.isDay != 0;
        weather.precipitation = weatherResponse.currently.precipIntensity;
        weather.snow = 0;
        weather.cloud = (int) (weatherResponse.currently.cloudCover * 100);
        weather.sunriseTime = new Date();
        weather.sunsetTime = new Date();
        weather.weatherCondition = mapWeatherCondition(weatherResponse.currently.icon);

        weather.daily = new ArrayList<>();
        for(DarkSkyWeatherResponse.Data day : weatherResponse.daily.data) {
            WeatherData weatherData = new WeatherData();
            weatherData.time = new Date(day.time);
            weatherData.temperatureC = day.temperatureMax;
            weatherData.weatherCondition = mapWeatherCondition(day.icon);
            weatherData.place = place;
            weather.daily.add(weatherData);
        }

        return weather;
    }

    private WeatherCondition mapWeatherCondition(String code) {
        switch(code) {
            case "clear-day":
            case "clear-night":
                return WeatherCondition.SUNNY;
            case "rain":
                return WeatherCondition.RAIN;
            case "snow":
                return WeatherCondition.SNOW;
            case "sleet":
                return WeatherCondition.SLEET;
            case "wind":
                return WeatherCondition.WIND;
            case "fog":
                return WeatherCondition.FOG;
            case "cloudy":
                return WeatherCondition.CLOUDY;
            case "partly-cloudy-day":
            case "partly-cloudy-night":
                return WeatherCondition.PARTLY_CLOUDY;

            default:
                return WeatherCondition.SUNNY;
        }
    }

    interface WeatherService {
        @GET("forecast/" + DARK_SKY_API_KEY + "/{latitude},{longitude}")
        Observable<DarkSkyWeatherResponse> getWeather(@Path("latitude") String latitude,
                                                      @Path("longitude") String longitude);
    }

}
