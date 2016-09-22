package com.ryanwelch.weather.domain.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class CurrentWeather implements Parcelable {

    // Data info

    public Place place;

    public Date updateTime; // Update time of data

    // Weather info

    public WeatherCondition weatherCondition;

    public Double temperatureC;

    public Double feelsLikeC;

    public Double pressure; // In hPa

    public Double humidity;

    public Double windSpeed;

    public Integer windDegree;

    public String windDirection;

    public Date sunriseTime;

    public Date sunsetTime;

    public boolean isDay;

    public Double precipitation;

    public Integer snow;

    public Integer cloud;

    public CurrentWeather() {}

    public CurrentWeather(Parcel source) {
        this.place = source.readParcelable(Place.class.getClassLoader());
        this.updateTime = new Date();
        this.updateTime.setTime(source.readLong());
        this.weatherCondition = WeatherCondition.values()[source.readInt()];
        this.temperatureC = source.readDouble();
        this.feelsLikeC = source.readDouble();
        this.pressure = source.readDouble();
        this.humidity = source.readDouble();
        this.windSpeed = source.readDouble();
        this.windDegree = source.readInt();
        this.windDirection = source.readString();
        this.sunriseTime = new Date();
        this.sunriseTime.setTime(source.readLong());
        this.sunsetTime = new Date();
        this.sunsetTime.setTime(source.readLong());
        this.isDay = source.readInt() == 1;
        this.precipitation = source.readDouble();
        this.snow = source.readInt();
        this.cloud = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(place, i);
        parcel.writeLong(updateTime.getTime());
        parcel.writeInt(weatherCondition.ordinal());
        parcel.writeDouble(temperatureC);
        parcel.writeDouble(feelsLikeC);
        parcel.writeDouble(pressure);
        parcel.writeDouble(humidity);
        parcel.writeDouble(windSpeed);
        parcel.writeInt(windDegree);
        parcel.writeString(windDirection);
        parcel.writeLong(sunriseTime != null ? sunriseTime.getTime() : 0);
        parcel.writeLong(sunsetTime != null ? sunsetTime.getTime() : 0);
        parcel.writeInt(isDay ? 1 : 0);
        parcel.writeDouble(precipitation);
        parcel.writeInt(snow);
        parcel.writeInt(cloud);
    }

    public static final Parcelable.Creator<CurrentWeather> CREATOR = new Parcelable.Creator<CurrentWeather>() {
        @Override
        public CurrentWeather createFromParcel(Parcel in) {
            return new CurrentWeather(in);
        }

        @Override
        public CurrentWeather[] newArray(int size) {
            return new CurrentWeather[size];
        }
    };
}
