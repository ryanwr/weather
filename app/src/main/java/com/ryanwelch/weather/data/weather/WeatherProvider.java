package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.data.helper.ResponseCallback;
import com.ryanwelch.weather.models.CurrentWeather;

public interface WeatherProvider {

    void getCurrentWeather(ResponseCallback<CurrentWeather> callback, double lat, double lon);
}
