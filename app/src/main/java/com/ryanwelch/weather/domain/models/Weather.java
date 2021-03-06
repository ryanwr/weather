package com.ryanwelch.weather.domain.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.ryanwelch.weather.data.db.WeatherTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class Weather implements Parcelable {

    public Long id;

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

    public List<WeatherData> daily = new ArrayList<>();

    public Weather() {}

    public Weather(Parcel source) {
        this.id = source.readLong();
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
        this.isDay = source.readInt() != 0;
        this.precipitation = source.readDouble();
        this.snow = source.readInt();
        this.cloud = source.readInt();
        source.readTypedList(daily, WeatherData.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
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
        parcel.writeTypedList(daily);
    }

    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };

    // SQL
    public Weather(Cursor cursor, Place place, List<WeatherData> daily) {
        id = cursor.getLong(cursor.getColumnIndex(WeatherTable.COLUMN_ID));
        this.place = place;
        this.daily = daily;
        updateTime = new Date(cursor.getLong(cursor.getColumnIndex(WeatherTable.COLUMN_UPDATE_TIME)));
        weatherCondition = WeatherCondition.values()[cursor.getInt(cursor.getColumnIndex(WeatherTable.COLUMN_WEATHER_CONDITION))];
        temperatureC = cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_TEMPERATURE));
        feelsLikeC = cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_FEELS_LIKE));
        pressure = cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_PRESSURE));
        humidity = cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_HUMIDITY));
        windSpeed = cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_WIND_SPEED));
        windDegree = cursor.getInt(cursor.getColumnIndex(WeatherTable.COLUMN_WIND_DEGREE));
        windDirection = cursor.getString(cursor.getColumnIndex(WeatherTable.COLUMN_WIND_DIRECTION));
        sunriseTime = new Date(cursor.getLong(cursor.getColumnIndex(WeatherTable.COLUMN_SUNRISE_TIME)));
        sunsetTime = new Date(cursor.getLong(cursor.getColumnIndex(WeatherTable.COLUMN_SUNSET_TIME)));
        isDay = cursor.getInt(cursor.getColumnIndex(WeatherTable.COLUMN_IS_DAY)) != 0;
        precipitation = cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_PRECIPITATION));
        snow = cursor.getInt(cursor.getColumnIndex(WeatherTable.COLUMN_SNOW));
        cloud = cursor.getInt(cursor.getColumnIndex(WeatherTable.COLUMN_CLOUD));
    }

    public ContentValues asContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeatherTable.COLUMN_LATITUDE, this.place.getLatitude());
        contentValues.put(WeatherTable.COLUMN_LONGITUDE, this.place.getLongitude());
        contentValues.put(WeatherTable.COLUMN_UPDATE_TIME, this.updateTime.getTime());
        contentValues.put(WeatherTable.COLUMN_WEATHER_CONDITION, this.weatherCondition.ordinal());
        contentValues.put(WeatherTable.COLUMN_TEMPERATURE, this.temperatureC);
        contentValues.put(WeatherTable.COLUMN_FEELS_LIKE, this.feelsLikeC);
        contentValues.put(WeatherTable.COLUMN_PRESSURE, this.pressure);
        contentValues.put(WeatherTable.COLUMN_HUMIDITY, this.humidity);
        contentValues.put(WeatherTable.COLUMN_WIND_SPEED, this.windSpeed);
        contentValues.put(WeatherTable.COLUMN_WIND_DEGREE, this.windDegree);
        contentValues.put(WeatherTable.COLUMN_WIND_DIRECTION, this.windDirection);
        contentValues.put(WeatherTable.COLUMN_SUNRISE_TIME, this.sunriseTime.getTime());
        contentValues.put(WeatherTable.COLUMN_SUNSET_TIME, this.sunsetTime.getTime());
        contentValues.put(WeatherTable.COLUMN_IS_DAY, this.isDay ? 1 : 0);
        contentValues.put(WeatherTable.COLUMN_PRECIPITATION, this.precipitation);
        contentValues.put(WeatherTable.COLUMN_SNOW, this.snow);
        contentValues.put(WeatherTable.COLUMN_CLOUD, this.cloud);

        return contentValues;
    }
}
