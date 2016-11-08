/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.injector.modules;


import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.ryanwelch.weather.data.db.CurrentWeatherTable;
import com.ryanwelch.weather.data.db.DbOpenHelper;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.PlaceSQLiteTypeMapping;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Provides
    @ApplicationScope
    SQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
        //context.deleteDatabase("weather_db");
        return new DbOpenHelper(context);
    }

    @Provides
    @ApplicationScope
    StorIOSQLite provideStorIO(SQLiteOpenHelper openHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(openHelper)
                .addTypeMapping(Place.class, new PlaceSQLiteTypeMapping())
                .addTypeMapping(CurrentWeather.class, SQLiteTypeMapping.<CurrentWeather>builder()
                        .putResolver(CurrentWeatherTable.PUT_RESOLVER)
                        .getResolver(CurrentWeatherTable.GET_RESOLVER)
                        .deleteResolver(CurrentWeatherTable.DELETE_RESOLVER)
                        .build())
                .build();
    }

}
