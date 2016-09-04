package com.ryanwelch.weather.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CurrentWeather {

    // Data info

    public Place place;

    public Date updateTime; // Update time of data

    // Weather info

    public WeatherCondition weatherCondition;

    //public WeatherIcon weatherIcon;

    public String weatherDescription;

    public Double temperature; // In Celsius

    public Double pressure; // In hPa

    public Double humidity;

    // Extra info

    public Date sunriseTime;

    public Date sunsetTime;

    public Integer rain;

    public Integer snow;

    public Integer cloudPercent;
}
