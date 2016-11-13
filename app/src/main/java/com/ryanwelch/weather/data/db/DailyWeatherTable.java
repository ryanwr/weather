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
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.operations.put.PutResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.pushtorefresh.storio.sqlite.queries.RawQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherData;

public class DailyWeatherTable {

    @NonNull
    public static final String TABLE = "daily_weather";

    @NonNull
    public static final String COLUMN_ID = "_id";

    @NonNull
    public static final String COLUMN_LATITUDE = "latitude";

    @NonNull
    public static final String COLUMN_LONGITUDE = "longitude";

    @NonNull
    public static final String COLUMN_TIME = "time";

    @NonNull
    public static final String COLUMN_WEATHER_CONDITION = "weather_condition";

    @NonNull
    public static final String COLUMN_TEMPERATURE = "temperature";

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
                + COLUMN_TIME + " TEXT NULL, "
                + COLUMN_WEATHER_CONDITION + " TEXT NULL, "
                + COLUMN_TEMPERATURE + " TEXT NULL "
                + ");";
    }

    @NonNull
    public static final PutResolver<WeatherData> PUT_RESOLVER = new DefaultPutResolver<WeatherData>() {
        @NonNull
        @Override
        protected InsertQuery mapToInsertQuery(@NonNull WeatherData object) {
            return InsertQuery.builder()
                    .table(TABLE)
                    .build();
        }

        @NonNull
        @Override
        protected UpdateQuery mapToUpdateQuery(@NonNull WeatherData object) {
            return UpdateQuery.builder()
                    .table(TABLE)
                    .where(COLUMN_LATITUDE + " = ? AND " + COLUMN_LONGITUDE + " = ? ")
                    .whereArgs(object.place.getLatitude(), object.place.getLongitude())
                    .build();
        }

        @NonNull
        @Override
        protected ContentValues mapToContentValues(@NonNull WeatherData object) {
            return object.asContentValues();
        }
    };

    @NonNull
    public static final GetResolver<WeatherData> GET_RESOLVER = new DefaultGetResolver<WeatherData>() {

        private final ThreadLocal<StorIOSQLite> storIOSQLiteFromPerformGet = new ThreadLocal<>();

        @NonNull
        @Override
        public WeatherData mapFromCursor(@NonNull Cursor cursor) {
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

            return new WeatherData(cursor, place);
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

    @NonNull
    public static final DeleteResolver<WeatherData> DELETE_RESOLVER = new DefaultDeleteResolver<WeatherData>() {
        @NonNull
        @Override
        public DeleteQuery mapToDeleteQuery(@NonNull WeatherData object) {
            return DeleteQuery.builder()
                    .table(TABLE)
                    .where(COLUMN_LATITUDE + " = ? AND " + COLUMN_LONGITUDE + " = ? ")
                    .whereArgs(object.place.getLatitude(), object.place.getLongitude())
                    .build();
        }
    };

    private DailyWeatherTable() {
        throw new IllegalStateException("No instances");
    }

}
