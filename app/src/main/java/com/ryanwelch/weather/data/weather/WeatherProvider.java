package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.models.CurrentWeather;
import com.ryanwelch.weather.models.Place;

import rx.Observable;

public interface WeatherProvider {

    Observable<CurrentWeather> getCurrentWeather(Place place);
}
