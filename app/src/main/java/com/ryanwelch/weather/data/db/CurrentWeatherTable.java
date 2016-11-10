/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;

import java.util.Date;

public class CurrentWeatherTable {

    @NonNull
    public static final String TABLE = "current_weather";

    @NonNull
    public static final String COLUMN_ID = "_id";

    @NonNull
    public static final String COLUMN_LATITUDE = "latitude";

    @NonNull
    public static final String COLUMN_LONGITUDE = "longitude";

    @NonNull
    public static final String COLUMN_NAME = "name";

    @NonNull
    public static final String COLUMN_REGION = "region";

    @NonNull
    public static final String COLUMN_COUNTRY = "country";

    @NonNull
    public static final String COLUMN_UPDATE_TIME = "update_time";

    @NonNull
    public static final String COLUMN_WEATHER_CONDITION = "weather_condition";

    @NonNull
    public static final String COLUMN_TEMPERATURE = "temperature";

    @NonNull
    public static final String COLUMN_FEELS_LIKE = "feels_like";

    @NonNull
    public static final String COLUMN_PRESSURE = "pressure";

    @NonNull
    public static final String COLUMN_HUMIDITY = "humidity";

    @NonNull
    public static final String COLUMN_WIND_SPEED = "wind_speed";

    @NonNull
    public static final String COLUMN_WIND_DEGREE = "wind_degree";

    @NonNull
    public static final String COLUMN_WIND_DIRECTION = "wind_direction";

    @NonNull
    public static final String COLUMN_SUNRISE_TIME = "sunrise_time";

    @NonNull
    public static final String COLUMN_SUNSET_TIME = "sunset_time";

    @NonNull
    public static final String COLUMN_IS_DAY = "is_day";

    @NonNull
    public static final String COLUMN_PRECIPITATION = "precipitation";

    @NonNull
    public static final String COLUMN_SNOW = "snow";

    @NonNull
    public static final String COLUMN_CLOUD = "cloud";

    @NonNull
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    // TODO: Use place id instead of serializing fields
    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_LATITUDE + " TEXT NULL, "
                + COLUMN_LONGITUDE + " TEXT NULL, "
                + COLUMN_NAME + " TEXT NULL, "
                + COLUMN_REGION + " TEXT NULL, "
                + COLUMN_COUNTRY + " TEXT NULL, "
                + COLUMN_UPDATE_TIME + " TEXT NULL, "
                + COLUMN_WEATHER_CONDITION + " TEXT NULL, "
                + COLUMN_TEMPERATURE + " TEXT NULL, "
                + COLUMN_FEELS_LIKE + " TEXT NULL, "
                + COLUMN_PRESSURE + " TEXT NULL, "
                + COLUMN_HUMIDITY + " TEXT NULL, "
                + COLUMN_WIND_SPEED + " TEXT NULL, "
                + COLUMN_WIND_DEGREE + " TEXT NULL, "
                + COLUMN_WIND_DIRECTION + " TEXT NULL, "
                + COLUMN_SUNRISE_TIME + " TEXT NULL, "
                + COLUMN_SUNSET_TIME + " TEXT NULL, "
                + COLUMN_IS_DAY + " TEXT NULL, "
                + COLUMN_PRECIPITATION + " TEXT NULL, "
                + COLUMN_SNOW + " TEXT NULL, "
                + COLUMN_CLOUD + " TEXT NULL "
                + ");";
    }

    @NonNull
    public static final PutResolver<Weather> PUT_RESOLVER = new DefaultPutResolver<Weather>() {
        @NonNull
        @Override
        protected InsertQuery mapToInsertQuery(@NonNull Weather object) {
            return InsertQuery.builder()
                    .table(TABLE)
                    .build();
        }

        @NonNull
        @Override
        protected UpdateQuery mapToUpdateQuery(@NonNull Weather object) {
            return UpdateQuery.builder()
                    .table(TABLE)
                    .where(COLUMN_LATITUDE + " = ? AND " + COLUMN_LONGITUDE + " = ? ")
                    .whereArgs(object.place.getLatitude(), object.place.getLongitude())
                    .build();
        }

        @NonNull
        @Override
        protected ContentValues mapToContentValues(@NonNull Weather object) {
            return object.asContentValues();
        }
    };

    @NonNull
    public static final GetResolver<Weather> GET_RESOLVER = new DefaultGetResolver<Weather>() {
        @NonNull
        @Override
        public Weather mapFromCursor(@NonNull Cursor cursor) {
            Weather object = new Weather();

            object.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            object.place = new Place(
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_REGION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE))
            );
            object.updateTime = new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_UPDATE_TIME)));
            object.weatherCondition = WeatherCondition.values()[cursor.getInt(cursor.getColumnIndex(COLUMN_WEATHER_CONDITION))];
            object.temperatureC = cursor.getDouble(cursor.getColumnIndex(COLUMN_TEMPERATURE));
            object.feelsLikeC = cursor.getDouble(cursor.getColumnIndex(COLUMN_FEELS_LIKE));
            object.pressure = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRESSURE));
            object.humidity = cursor.getDouble(cursor.getColumnIndex(COLUMN_HUMIDITY));
            object.windSpeed = cursor.getDouble(cursor.getColumnIndex(COLUMN_WIND_SPEED));
            object.windDegree = cursor.getInt(cursor.getColumnIndex(COLUMN_WIND_DEGREE));
            object.windDirection = cursor.getString(cursor.getColumnIndex(COLUMN_WIND_DIRECTION));
            object.sunriseTime = new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_SUNRISE_TIME)));
            object.sunsetTime = new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_SUNSET_TIME)));
            object.isDay = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DAY)) != 0;
            object.precipitation = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRECIPITATION));
            object.snow = cursor.getInt(cursor.getColumnIndex(COLUMN_SNOW));
            object.cloud = cursor.getInt(cursor.getColumnIndex(COLUMN_CLOUD));

            return object;
        }
    };

    @NonNull
    public static final DeleteResolver<Weather> DELETE_RESOLVER = new DefaultDeleteResolver<Weather>() {
        @NonNull
        @Override
        public DeleteQuery mapToDeleteQuery(@NonNull Weather object) {
            return DeleteQuery.builder()
                    .table(TABLE)
                    .where(COLUMN_LATITUDE + " = ? AND " + COLUMN_LONGITUDE + " = ? ")
                    .whereArgs(object.place.getLatitude(), object.place.getLongitude())
                    .build();
        }
    };

}
