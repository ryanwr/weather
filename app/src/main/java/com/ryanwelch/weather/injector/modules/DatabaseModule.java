/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.injector.modules;


import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.ryanwelch.weather.data.db.DailyWeatherTable;
import com.ryanwelch.weather.data.db.WeatherTable;
import com.ryanwelch.weather.data.db.DbOpenHelper;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.PlaceSQLiteTypeMapping;
import com.ryanwelch.weather.domain.models.WeatherData;
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
                .addTypeMapping(Weather.class, SQLiteTypeMapping.<Weather>builder()
                        .putResolver(WeatherTable.PUT_RESOLVER)
                        .getResolver(WeatherTable.GET_RESOLVER)
                        .deleteResolver(WeatherTable.DELETE_RESOLVER)
                        .build())
                .addTypeMapping(WeatherData.class, SQLiteTypeMapping.<WeatherData>builder()
                        .putResolver(DailyWeatherTable.PUT_RESOLVER)
                        .getResolver(DailyWeatherTable.GET_RESOLVER)
                        .deleteResolver(DailyWeatherTable.DELETE_RESOLVER)
                        .build())
                .build();
    }

}
