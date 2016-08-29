package com.ryanwelch.weather.models.responses;

import com.google.gson.annotations.SerializedName;

public class BaseWeatherResponse {

    @SerializedName("cod")
    public int cod;

}
