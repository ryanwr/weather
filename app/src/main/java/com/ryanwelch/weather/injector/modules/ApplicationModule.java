package com.ryanwelch.weather.injector.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.ryanwelch.weather.data.place.PlaceMemoryDataSource;
import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.data.place.PlaceSharedPreferencesDataSource;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;
import com.ryanwelch.weather.ui.navigation.Navigator;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationScope
    Context providesApplicationContext() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    Navigator providesNavigator() {
        return new Navigator();
    }

    @Provides
    @ApplicationScope
    ReactiveLocationProvider providesLocationProvider(Context context) {
        return new ReactiveLocationProvider(context);
    }

    @Provides
    @ApplicationScope
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @ApplicationScope
    PlaceRepository providePlaceRepository(SharedPreferences preferences, Gson gson) {
        return new PlaceRepository(new PlaceMemoryDataSource(), new PlaceSharedPreferencesDataSource(preferences, gson));
    }
}
