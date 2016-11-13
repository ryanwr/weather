/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather.darksky;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DarkSkyWeatherResponse {

    @SerializedName("latitude")
    Double latitude;

    @SerializedName("longitude")
    Double longitude;

    @SerializedName("timezone")
    String timezone;

    @SerializedName("currently")
    Currently currently;

    @SerializedName("daily")
    Daily daily;

    public static class Currently {
        @SerializedName("time")
        public Long time;

        @SerializedName("summary")
        public String summary;

        @SerializedName("icon")
        public String icon;

        @SerializedName("nearestStormDistance")
        public Double nearestStormDistance;

        @SerializedName("precipIntensity")
        public Double precipIntensity;

        @SerializedName("precipIntensityError")
        public Double precipIntensityError;

        @SerializedName("precipProbability")
        public Double precipProbability;

        @SerializedName("precipType")
        public String precipType;

        @SerializedName("temperature")
        public Double temperature;

        @SerializedName("apparentTemperature")
        public Double apparentTemperature;

        @SerializedName("dewPoint")
        public Double dewPoint;

        @SerializedName("humidity")
        public Double humidity;

        @SerializedName("windSpeed")
        public Double windSpeed;

        @SerializedName("windBearing")
        public Integer windBearing;

        @SerializedName("visibility")
        public Double visibility;

        @SerializedName("cloudCover")
        public Double cloudCover;

        @SerializedName("pressure")
        public Double pressure;

        @SerializedName("ozone")
        public Double ozone;
    }

    public static class Daily {
        @SerializedName("summary")
        public String summary;

        @SerializedName("icon")
        public String icon;

        @SerializedName("data")
        public List<Data> data;
    }

    public static class Data {
        @SerializedName("time")
        public Long time;

        @SerializedName("summary")
        public String summary;

        @SerializedName("icon")
        public String icon;

        @SerializedName("sunriseTime")
        public Long sunriseTime;

        @SerializedName("sunsetTime")
        public Long sunsetTime;

        @SerializedName("moonPhase")
        public Double moonPhase;

        @SerializedName("precipIntensity")
        public Double precipIntensity;

        @SerializedName("precipIntensityMax")
        public Double precipIntensityMax;

        @SerializedName("precipIntensityMaxTime")
        public Long precipIntensityMaxTime;

        @SerializedName("precipProbability")
        public Double precipProbability;

        @SerializedName("precipType")
        public String precipType;

        @SerializedName("temperatureMin")
        public Double temperatureMin;

        @SerializedName("temperatureMinTime")
        public Long temperatureMinTime;

        @SerializedName("temperatureMax")
        public Double temperatureMax;

        @SerializedName("temperatureMaxTime")
        public Long temperatureMaxTime;

        @SerializedName("apparentTemperatureMin")
        public Double apparentTemperatureMin;

        @SerializedName("apparentTemperatureMinTime")
        public Long apparentTemperatureMinTime;

        @SerializedName("apparentTemperatureMax")
        public Double apparentTemperatureMax;

        @SerializedName("apparentTemperatureMaxTime")
        public Long apparentTemperatureMaxTime;

        @SerializedName("dewPoint")
        public Double dewPoint;

        @SerializedName("humidity")
        public Double humidity;

        @SerializedName("windSpeed")
        public Double windSpeed;

        @SerializedName("windBearing")
        public Integer windBearing;

        @SerializedName("visibility")
        public Double visibility;

        @SerializedName("cloudCover")
        public Double cloudCover;

        @SerializedName("pressure")
        public Double pressure;

        @SerializedName("ozone")
        public Double ozone;
    }

}
