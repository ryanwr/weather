package com.ryanwelch.weather.data;

import com.ryanwelch.weather.models.CurrentWeather;

public interface WeatherProvider {

    void getCurrentWeather(ResponseCallback<CurrentWeather> callback, double lat, double lon);
}
