package com.ryanwelch.weather.data.models;

import com.google.gson.annotations.SerializedName;
import com.ryanwelch.weather.domain.models.Place;

public class ApixuWeatherResponse {

    @SerializedName("location")
    public Place place;

    @SerializedName("current")
    public Current current;

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
