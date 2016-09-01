package com.ryanwelch.weather.data;

import com.ryanwelch.weather.models.CurrentWeather;
import com.ryanwelch.weather.models.WeatherCondition;

import java.util.Date;

public class WeatherProviderDebugImpl implements WeatherProvider {

    private int count = 0;

    @Override
    public void getCurrentWeather(ResponseCallback<CurrentWeather> callback, double lat, double lon) {
        CurrentWeather reply = new CurrentWeather();
        count++;
        switch(count) {
            case 1:
                reply.updateTime = new Date();
                reply.weatherCondition = WeatherCondition.RAIN;
                reply.temperature = 17.0;
                reply.countryCode = "GB";
                reply.cityName = "London";
                break;
            case 2:
                reply.updateTime = new Date();
                reply.weatherCondition = WeatherCondition.SUNNY;
                reply.temperature = 23.0;
                reply.countryCode = "US";
                reply.cityName = "New York";
                break;
            case 3:
                reply.updateTime = new Date();
                reply.weatherCondition = WeatherCondition.SNOW;
                reply.temperature = 5.0;
                reply.countryCode = "CA";
                reply.cityName = "Alaska";

                count = 0;
                break;
        }
        callback.onSuccess(reply);
    }

}
