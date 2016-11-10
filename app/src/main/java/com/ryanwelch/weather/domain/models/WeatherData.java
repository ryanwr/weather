/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.domain.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class WeatherData implements Parcelable {

    public WeatherCondition weatherCondition;

    public Double temperatureC;

    public Date time;

    public WeatherData() {}

    public WeatherData(WeatherCondition weatherCondition, double temperature, Date time) {
        this.weatherCondition = weatherCondition;
        this.temperatureC = temperature;
        this.time = time;
    }

    public WeatherData(Parcel source) {
        this.weatherCondition = WeatherCondition.values()[source.readInt()];
        this.temperatureC = source.readDouble();
        this.time = new Date();
        this.time.setTime(source.readLong());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(weatherCondition.ordinal());
        parcel.writeDouble(temperatureC);
        parcel.writeLong(time != null ? time.getTime() : 0);
    }

    public static final Parcelable.Creator<WeatherData> CREATOR = new Parcelable.Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

}
