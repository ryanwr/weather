/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.domain.models;


import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.ryanwelch.weather.data.db.DailyWeatherTable;

import java.util.Date;

public class WeatherData implements Parcelable {

    public Long id;

    public Place place;

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
        this.id = source.readLong();
        this.place = source.readParcelable(Place.class.getClassLoader());
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
        parcel.writeLong(id);
        parcel.writeParcelable(place, i);
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

    // SQL
    public WeatherData(Cursor cursor, Place place) {
        id = cursor.getLong(cursor.getColumnIndex(DailyWeatherTable.COLUMN_ID));
        this.place = place;
        time = new Date(cursor.getLong(cursor.getColumnIndex(DailyWeatherTable.COLUMN_TIME)));
        weatherCondition = WeatherCondition.values()[cursor.getInt(cursor.getColumnIndex(DailyWeatherTable.COLUMN_WEATHER_CONDITION))];
        temperatureC = cursor.getDouble(cursor.getColumnIndex(DailyWeatherTable.COLUMN_TEMPERATURE));
    }

    public ContentValues asContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DailyWeatherTable.COLUMN_LATITUDE, this.place.getLatitude());
        contentValues.put(DailyWeatherTable.COLUMN_LONGITUDE, this.place.getLongitude());
        contentValues.put(DailyWeatherTable.COLUMN_TIME, this.time.getTime());
        contentValues.put(DailyWeatherTable.COLUMN_WEATHER_CONDITION, this.weatherCondition.ordinal());
        contentValues.put(DailyWeatherTable.COLUMN_TEMPERATURE, this.temperatureC);

        return contentValues;
    }

}
