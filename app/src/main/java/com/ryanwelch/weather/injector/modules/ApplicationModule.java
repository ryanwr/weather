package com.ryanwelch.weather.injector.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ryanwelch.weather.WeatherApplication;
import com.ryanwelch.weather.data.place.PlaceMemoryDataSource;
import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;
import com.ryanwelch.weather.ui.navigation.Navigator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application mApplication;

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
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @ApplicationScope
    PlaceRepository providePlaceRepository() {
        return new PlaceRepository(new PlaceMemoryDataSource());
    }
}
