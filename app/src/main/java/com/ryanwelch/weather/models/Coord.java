package com.ryanwelch.weather.models;

import com.google.gson.annotations.SerializedName;

public class Coord {

    @SerializedName("lon")
    public Double lon;

    @SerializedName("lat")
    public Double lat;

}
