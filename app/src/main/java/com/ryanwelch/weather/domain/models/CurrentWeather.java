package com.ryanwelch.weather.domain.models;

import java.util.Date;

public class CurrentWeather {

    // Data info

    public Place place;

    public Date updateTime; // Update time of data

    // Weather info

    public WeatherCondition weatherCondition;

    public Double temperature;

    public Double feelsLike;

    public Double pressure; // In hPa

    public Double humidity;

    public Double windSpeed;

    public Integer windDegree;

    public String windDirection;

    public Date sunriseTime;

    public Date sunsetTime;

    public boolean isDay;

    public Double precipitation;

    public Integer snow;

    public Integer cloud;
}
