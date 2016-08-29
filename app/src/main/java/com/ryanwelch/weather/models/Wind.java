package com.ryanwelch.weather.models;

import com.google.gson.annotations.SerializedName;

public class Wind {
    @SerializedName("speed")
    public Double speed;

    @SerializedName("deg")
    public Integer deg;
}
