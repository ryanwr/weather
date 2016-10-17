/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;

public class MockWeatherRepository implements WeatherDataSource {

    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        return Observable.just(createWeather(place)).delay(2, TimeUnit.SECONDS);
    }

    private CurrentWeather createWeather(Place place) {
        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.place = place;
        currentWeather.updateTime = new Date();
        currentWeather.temperatureC = 23.0;
        currentWeather.feelsLikeC = 26.0;
        currentWeather.pressure = 5.0;
        currentWeather.humidity = 0.4;
        currentWeather.windSpeed = 4.3;
        currentWeather.windDegree = 45;
        currentWeather.windDirection = "NSW";
        currentWeather.isDay = true;
        currentWeather.precipitation = 0.0;
        currentWeather.snow = 0;
        currentWeather.cloud = 50;
        currentWeather.sunriseTime = new Date();
        currentWeather.sunsetTime = new Date();

        switch((int) (Math.random() * 3)) {
            case 0:
                currentWeather.weatherCondition = WeatherCondition.MOSTLY_SUNNY;
                break;
            case 1:
                currentWeather.weatherCondition = WeatherCondition.SUNNY;
                break;
            case 2:
                currentWeather.weatherCondition = WeatherCondition.RAIN;
                break;
            case 3:
                currentWeather.weatherCondition = WeatherCondition.THUNDERSTORMS;
                break;
            default:
                currentWeather.weatherCondition = WeatherCondition.SUNNY;
                break;
        }

        return currentWeather;
    }

}
