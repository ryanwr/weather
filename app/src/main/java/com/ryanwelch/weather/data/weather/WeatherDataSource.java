package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;

import rx.Observable;

public interface WeatherDataSource {

    Observable<Weather> getWeather(Place place);
}
