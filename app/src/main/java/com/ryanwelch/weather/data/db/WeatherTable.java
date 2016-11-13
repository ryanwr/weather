/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;
import com.ryanwelch.weather.domain.models.WeatherData;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import timber.log.Timber;

public class WeatherTable {

    @NonNull
    public static final String TABLE = "current_weather";

    @NonNull
    public static final String COLUMN_ID = "_id";

    @NonNull
    public static final String COLUMN_LATITUDE = "latitude";

    @NonNull
    public static final String COLUMN_LONGITUDE = "longitude";

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

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_LATITUDE + " TEXT NULL, "
                + COLUMN_LONGITUDE + " TEXT NULL, "
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
    public static final PutResolver<Weather> PUT_WEATHER_RESOLVER = new DefaultPutResolver<Weather>() {
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
    public static final PutResolver<Weather> PUT_RESOLVER = new PutResolver<Weather>() {

        @NonNull
        @Override
        public PutResult performPut(@NonNull StorIOSQLite storIOSQLite, @NonNull Weather object) {
            final List<Object> objectsToPut = new ArrayList<>(object.daily.size());
            objectsToPut.addAll(object.daily);

            storIOSQLite
                    .put()
                    .objects(objectsToPut)
                    .prepare()
                    .executeAsBlocking();

            storIOSQLite
                    .put()
                    .object(object)
                    .withPutResolver(PUT_WEATHER_RESOLVER)
                    .prepare()
                    .executeAsBlocking();

            final Set<String> affectedTables = new HashSet<>(2);

            affectedTables.add(WeatherTable.TABLE);
            affectedTables.add(DailyWeatherTable.TABLE);

            return PutResult.newUpdateResult(objectsToPut.size() + 1, affectedTables);
        }
    };

    /**
     * Get Resolver for Weather
     */
    @NonNull
    public static final GetResolver<Weather> GET_RESOLVER = new DefaultGetResolver<Weather>() {

        // TODO: Use a raw query with sql JOIN

        private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet = new ThreadLocal<>();

        @NonNull
        @Override
        public Weather mapFromCursor(@NonNull Cursor cursor) {
            final StorIOSQLite storIOSQLite = storIOSQLiteFromPerformGet.get();

            double latitude = cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(WeatherTable.COLUMN_LONGITUDE));

            final Place place = storIOSQLite
                    .get()
                    .object(Place.class)
                    .withQuery(Query.builder()
                            .table(PlaceTable.TABLE)
                            .where(PlaceTable.COLUMN_LATITUDE + " = ? AND " + PlaceTable.COLUMN_LONGITUDE + " = ? ")
                            .whereArgs(latitude, longitude)
                            .build())
                    .prepare()
                    .executeAsBlocking();

            // TODO: Manually lookup daily weather and set place to place found above. Using its Get Resolver will lookup a place for every WeatherData object!
            final List<WeatherData> dailyWeather = storIOSQLite
                    .get()
                    .listOfObjects(WeatherData.class)
                    .withQuery(Query.builder()
                            .table(DailyWeatherTable.TABLE)
                            .where(DailyWeatherTable.COLUMN_LATITUDE + " = ? AND " + DailyWeatherTable.COLUMN_LONGITUDE + " = ? ")
                            .whereArgs(latitude, longitude)
                            .build())
                    .prepare()
                    .executeAsBlocking();

            return new Weather(cursor, place, dailyWeather);
        }

        @NonNull
        @Override
        public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull RawQuery rawQuery) {
            storIOSQLiteFromPerformGet.set(storIOSQLite);
            return storIOSQLite.lowLevel().rawQuery(rawQuery);
        }

        @NonNull
        @Override
        public Cursor performGet(@NonNull StorIOSQLite storIOSQLite, @NonNull Query query) {
            storIOSQLiteFromPerformGet.set(storIOSQLite);
            return storIOSQLite.lowLevel().query(query);
        }
    };

    /**
     * Default Delete Resolver used by the Delete Resolver, only deletes this object and does not affect related objects
     */
    @NonNull
    private static final DeleteResolver<Weather> DELETE_WEATHER_RESOLVER = new DefaultDeleteResolver<Weather>() {
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

    /**
     * Delete Resolver for Weather, also deletes all WeatherData objects associated to the object
     */
    @NonNull
    public static final DeleteResolver<Weather> DELETE_RESOLVER = new DeleteResolver<Weather>() {

        @NonNull
        @Override
        public DeleteResult performDelete(@NonNull StorIOSQLite storIOSQLite, @NonNull Weather object) {
            final List<Object> objectsToDelete = new ArrayList<>(object.daily.size());
            objectsToDelete.addAll(object.daily);

            storIOSQLite
                    .delete()
                    .objects(objectsToDelete)
                    .prepare()
                    .executeAsBlocking();

            storIOSQLite
                    .delete()
                    .object(object)
                    .withDeleteResolver(DELETE_WEATHER_RESOLVER)
                    .prepare()
                    .executeAsBlocking();

            final Set<String> affectedTables = new HashSet<>(2);

            affectedTables.add(WeatherTable.TABLE);
            affectedTables.add(DailyWeatherTable.TABLE);

            return DeleteResult.newInstance(objectsToDelete.size() + 1, affectedTables);
        }

    };

    private WeatherTable() {
        throw new IllegalStateException("No instances");
    }

}
