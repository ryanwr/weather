package com.ryanwelch.weather.data.models;

import com.google.gson.annotations.SerializedName;
import com.ryanwelch.weather.models.Place;

public class ApixuWeatherResponse {

    @SerializedName("location")
    public Place place;

}
