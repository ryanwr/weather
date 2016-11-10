package com.ryanwelch.weather.domain.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.ryanwelch.weather.data.db.CurrentWeatherTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Weather implements Parcelable {

    public Integer id;

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

    public ContentValues asContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CurrentWeatherTable.COLUMN_ID, this.id);
        contentValues.put(CurrentWeatherTable.COLUMN_LATITUDE, this.place.getLatitude());
        contentValues.put(CurrentWeatherTable.COLUMN_LONGITUDE, this.place.getLongitude());
        contentValues.put(CurrentWeatherTable.COLUMN_NAME, this.place.getName());
        contentValues.put(CurrentWeatherTable.COLUMN_REGION, this.place.getRegion());
        contentValues.put(CurrentWeatherTable.COLUMN_COUNTRY, this.place.getCountry());
        contentValues.put(CurrentWeatherTable.COLUMN_UPDATE_TIME, this.updateTime.getTime());
        contentValues.put(CurrentWeatherTable.COLUMN_WEATHER_CONDITION, this.weatherCondition.ordinal());
        contentValues.put(CurrentWeatherTable.COLUMN_TEMPERATURE, this.temperatureC);
        contentValues.put(CurrentWeatherTable.COLUMN_FEELS_LIKE, this.feelsLikeC);
        contentValues.put(CurrentWeatherTable.COLUMN_PRESSURE, this.pressure);
        contentValues.put(CurrentWeatherTable.COLUMN_HUMIDITY, this.humidity);
        contentValues.put(CurrentWeatherTable.COLUMN_WIND_SPEED, this.windSpeed);
        contentValues.put(CurrentWeatherTable.COLUMN_WIND_DEGREE, this.windDegree);
        contentValues.put(CurrentWeatherTable.COLUMN_WIND_DIRECTION, this.windDirection);
        contentValues.put(CurrentWeatherTable.COLUMN_SUNRISE_TIME, this.sunriseTime.getTime());
        contentValues.put(CurrentWeatherTable.COLUMN_SUNSET_TIME, this.sunsetTime.getTime());
        contentValues.put(CurrentWeatherTable.COLUMN_IS_DAY, this.isDay ? 1 : 0);
        contentValues.put(CurrentWeatherTable.COLUMN_PRECIPITATION, this.precipitation);
        contentValues.put(CurrentWeatherTable.COLUMN_SNOW, this.snow);
        contentValues.put(CurrentWeatherTable.COLUMN_CLOUD, this.cloud);

        return contentValues;
    }
}
