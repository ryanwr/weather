package com.ryanwelch.weather.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CurrentWeather {

    // Data info

    public Coord location; // Location of data

    public Date updateTime; // Update time of data

    public String cityName; // e.g. London

    public String countryCode; // e.g. GB

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

    public Integer rain; // Volume per 3 hours

    public Integer snow; // Volume per 3 hours

    public Integer cloudPercent;

    public Wind wind;
}
