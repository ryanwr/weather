/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.db;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.queries.Query;

public class PlaceTable {

    @NonNull
    public static final String TABLE = "places";

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
    public static final Query QUERY_ALL = Query.builder()
            .table(TABLE)
            .build();

    @NonNull
    public static String getCreateTableQuery() {
        return "CREATE TABLE " + TABLE + "("
                + COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, "
                + COLUMN_LATITUDE + " TEXT NULL, "
                + COLUMN_LONGITUDE + " TEXT NULL, "
                + COLUMN_NAME + " TEXT NULL, "
                + COLUMN_REGION + " TEXT NULL, "
                + COLUMN_COUNTRY + " TEXT NULL "
                + ");";
    }
}
