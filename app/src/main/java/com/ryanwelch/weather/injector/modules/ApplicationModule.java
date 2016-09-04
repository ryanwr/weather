package com.ryanwelch.weather.injector.modules;

import android.app.Application;
import android.content.Context;

import com.ryanwelch.weather.WeatherApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private WeatherApplication mApplication;

    public ApplicationModule(WeatherApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context providesApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }
}
