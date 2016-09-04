package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.data.ResponseCallback;
import com.ryanwelch.weather.models.CurrentWeather;
import com.ryanwelch.weather.models.Place;

public interface WeatherProvider {

    void getCurrentWeather(ResponseCallback<CurrentWeather> callback, Place place);
}
