package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import rx.Observable;

public interface WeatherDataSource {

    Observable<CurrentWeather> getCurrentWeather(Place place);
}
