package com.ryanwelch.weather;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ryanwelch.weather.injector.components.ApplicationComponent;
import com.ryanwelch.weather.injector.components.DaggerApplicationComponent;
import com.ryanwelch.weather.injector.modules.ApplicationModule;

public class WeatherApplication extends Application {

    private static final String TAG = "WeatherApplication";

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        Log.i(TAG, "Initialized app");
    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static WeatherApplication from(Context context) {
        return (WeatherApplication) context.getApplicationContext();
    }
}
