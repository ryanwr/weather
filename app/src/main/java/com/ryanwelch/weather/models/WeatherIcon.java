package com.ryanwelch.weather.models;

import com.ryanwelch.weather.R;

public enum WeatherIcon {
    SUNNY(R.color.sunnyBg),
    //MOSTLY_SUNNY(R.color.sunnyBg),
    CLEAR(R.color.clearBg),
    CLOUDY(R.color.cloudyBg),
    RAIN(R.color.rainBg),
    SNOW(R.color.snowBg),
    THUNDERSTORM(R.color.stormBg);

    private int mColor;

    WeatherIcon(int color) {
        mColor = color;
    }

    public int getColor() {
        return mColor;
    }
}
