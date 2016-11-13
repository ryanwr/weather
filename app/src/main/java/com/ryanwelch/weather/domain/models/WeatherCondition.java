package com.ryanwelch.weather.domain.models;

public enum WeatherCondition {
    SUNNY("Sunny", "Clear", WeatherIcon.SUNNY, WeatherIcon.CLEAR),
    MOSTLY_SUNNY("Mostly Sunny", WeatherIcon.SUNNY),
    PARTLY_CLOUDY("Partly Cloudy", WeatherIcon.CLOUDY),
    MOSTLY_CLOUDY("Mostly Cloudy", WeatherIcon.CLOUDY),
    CLOUDY("Cloudy", WeatherIcon.CLOUDY),
    OVERCAST("Overcast", WeatherIcon.CLOUDY),

    MIST("Mist", WeatherIcon.CLOUDY),
    FOG("Fog", WeatherIcon.CLOUDY),
    HAZE("Haze", WeatherIcon.CLOUDY),
    SMOKE("Smoke", WeatherIcon.CLOUDY),
    DUST("Dust", WeatherIcon.CLOUDY),
    WIND("Wind", WeatherIcon.CLOUDY),

    LIGHT_DRIZZLE("Light Drizzle", WeatherIcon.RAIN),
    DRIZZLE("Drizzle", WeatherIcon.RAIN),
    FREEZING_DRIZZLE("Freezing Drizzle", WeatherIcon.RAIN),
    DRIZZLE_SHOWERS("Drizzle Showers", WeatherIcon.RAIN),

    CHANCE_OF_RAIN("Chance of Rain", WeatherIcon.RAIN),
    SCATTERED_SHOWERS("Scattered Showers", WeatherIcon.RAIN),
    SHOWERS("Showers", WeatherIcon.RAIN),
    LIGHT_RAIN("Light Rain", WeatherIcon.RAIN),
    RAIN("Rain", WeatherIcon.RAIN),
    FREEZING_RAIN("Freezing Rain", WeatherIcon.RAIN),

    CHANCE_OF_SNOW("Chance of Snow", WeatherIcon.SNOW),
    SNOW_SHOWERS("Snow Showers", WeatherIcon.SNOW),
    LIGHT_SNOW("Light Snow", WeatherIcon.SNOW),
    SNOW("Snow", WeatherIcon.SNOW),
    BLIZZARD("Blizzard", WeatherIcon.SNOW),

    SLEET_SHOWERS("Sleet Showers", WeatherIcon.RAIN),
    SLEET("Sleet", WeatherIcon.RAIN),

    CHANCE_OF_STORM("Chance of Storm", WeatherIcon.THUNDERSTORM),
    STORM("Storm", WeatherIcon.THUNDERSTORM),
    SNOW_THUNDERSTORMS("Snow Thunderstorm", WeatherIcon.THUNDERSTORM),
    CHANCE_OF_THUNDERSTORM("Chance of Thunderstorm", WeatherIcon.THUNDERSTORM),
    THUNDERSTORMS("Thunderstorm", WeatherIcon.THUNDERSTORM);

    private String mName;
    private String mNightName;

    private WeatherIcon mIcon;
    private WeatherIcon mNightIcon;

    WeatherCondition(String name, WeatherIcon icon) {
        this.mName = name;
        this.mIcon = icon;
    }

    WeatherCondition(String dayName, String nightName, WeatherIcon dayIcon, WeatherIcon nightIcon) {
        this.mName = dayName;
        this.mNightName = nightName;
        this.mIcon = dayIcon;
        this.mNightIcon = nightIcon;
    }

    public String getName() {
        return mName;
    }

    public String getNightName() {
        return mNightName != null ? mNightName : mName;
    }

    public WeatherIcon getIcon() {
        return mIcon;
    }

    public WeatherIcon getNightIcon() {
        return mNightIcon != null ? mNightIcon : mIcon;
    }
}
