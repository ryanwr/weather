package com.ryanwelch.weather.models;

public enum WeatherCondition {
    SUNNY("Sunny", "Clear"),
    MOSTLY_SUNNY("Mostly Sunny"),
    PARTLY_CLOUDY("Partly Cloudy"),
    MOSTLY_CLOUDY("Mostly Cloudy"),
    CLOUDY("Cloudy"),
    OVERCAST("Overcast"),

    MIST("Mist"),
    FOG("Fog"),
    HAZE("Haze"),
    SMOKE("Smoke"),
    DUST("Dust"),

    LIGHT_DRIZZLE("Light Drizzle"),
    DRIZZLE("Drizzle"),
    FREEZING_DRIZZLE("Freezing Drizzle"),
    DRIZZLE_SHOWERS("Drizzle Showers"),

    CHANCE_OF_RAIN("Chance of Rain"),
    SCATTERED_SHOWERS("Scattered Showers"),
    SHOWERS("Showers"),
    LIGHT_RAIN("Light Rain"),
    RAIN("Rain"),
    FREEZING_RAIN("Freezing Rain"),

    CHANCE_OF_SNOW("Chance of Snow"),
    SNOW_SHOWERS("Snow Showers"),
    LIGHT_SNOW("Light Snow"),
    SNOW("Snow"),
    BLIZZARD("Blizzard"),

    SLEET_SHOWERS("Sleet Showers"),
    SLEET("Sleet"),

    CHANCE_OF_STORM("Chance of Storm"),
    STORM("Storm"),
    SNOW_THUNDERSTORMS("Snow Thunderstorm"),
    CHANCE_OF_THUNDERSTORM("Chance of Thunderstorm"),
    THUNDERSTORMS("Thunderstorm");

    private String name;
    private String nightName;

    WeatherCondition(String dayName) {
        this.name = dayName;
    }

    WeatherCondition(String dayName, String nightName) {
        this.name = dayName;
        this.nightName = nightName;
    }

    public String getName() {
        return name;
    }

    public String getNightName() {
        return nightName != null ? nightName : name;
    }
}
