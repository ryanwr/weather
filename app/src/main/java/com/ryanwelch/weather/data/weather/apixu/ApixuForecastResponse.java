/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather.apixu;

import com.google.gson.annotations.SerializedName;
import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

public class ApixuForecastResponse {

    @SerializedName("location")
    public Place place;

    @SerializedName("current")
    public Current current;

    @SerializedName("forecast")
    public Forecast forecast;

    public static class Forecast {
        @SerializedName("forecastday")
        public List<ForecastDay> daily;
    }

    public static class ForecastDay {
        @SerializedName("date")
        public String date;

        @SerializedName("date_epoch")
        public Long dateEpoch;

        @SerializedName("day")
        public Day day;

        // TODO: Astro
    }

    public static class Day {
        @SerializedName("maxtemp_c")
        public Double maxTempC;

        @SerializedName("maxtemp_f")
        public Double maxTempF;

        @SerializedName("mintemp_c")
        public Double minTempC;

        @SerializedName("mintemp_f")
        public Double minTempF;

        @SerializedName("avgtemp_c")
        public Double avgTempC;

        @SerializedName("avgtemp_f")
        public Double avgTempF;

        @SerializedName("maxwind_mph")
        public Double maxWindMph;

        @SerializedName("maxwind_kph")
        public Double maxWindKph;

        @SerializedName("totalprecip_mm")
        public Double totalPrecipMm;

        @SerializedName("totalprecip_in")
        public Double totalPrecipIn;

        @SerializedName("condition")
        public Condition condition;
    }

    public static class Current {
        @SerializedName("last_updated_epoch")
        public Long lastUpdatedEpoch;

        @SerializedName("last_updated")
        public String lastUpdated;

        @SerializedName("temp_c")
        public Double tempC;

        @SerializedName("temp_f")
        public Double tempF;

        @SerializedName("is_day")
        public Integer isDay;

        @SerializedName("wind_mph")
        public Double windMph;

        @SerializedName("wind_kph")
        public Double windKph;

        @SerializedName("wind_degree")
        public Integer windDegree;

        @SerializedName("wind_dir")
        public String windDir;

        @SerializedName("pressure_mb")
        public Double pressureMb;

        @SerializedName("pressure_in")
        public Double pressureIn;

        @SerializedName("precip_mm")
        public Double precipMm;

        @SerializedName("precip_in")
        public Double precipIn;

        @SerializedName("humidity")
        public Double humidity;

        @SerializedName("cloud")
        public Integer cloud;

        @SerializedName("feelslike_c")
        public Double feelsLikeC;

        @SerializedName("feelslike_f")
        public Double feelsLikeF;

        @SerializedName("condition")
        public Condition condition;
    }

    public static class Condition {
        @SerializedName("text")
        public String text;

        @SerializedName("icon")
        public String icon;

        @SerializedName("code")
        public Integer code;
    }
}
