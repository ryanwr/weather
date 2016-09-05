package com.ryanwelch.weather;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ryanwelch.weather.injector.components.ApplicationComponent;
import com.ryanwelch.weather.injector.components.DaggerApplicationComponent;
import com.ryanwelch.weather.injector.components.NetComponent;
import com.ryanwelch.weather.injector.modules.ApplicationModule;
import com.ryanwelch.weather.injector.modules.NetModule;

public class WeatherApplication extends Application {

    private static final String TAG = "WeatherApplication";

    private ApplicationComponent mAppComponent;
    private NetComponent mNetComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        Log.i(TAG, "Initialized app");
    }

    private void initializeInjector() {
        mAppComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        mNetComponent = mAppComponent.plus(new NetModule(""));
    }

    public ApplicationComponent getAppComponent() {
        return mAppComponent;
    }

    public NetComponent getNetComponent() {
        return mNetComponent;
    }

    public static WeatherApplication from(Context context) {
        return (WeatherApplication) context.getApplicationContext();
    }
}
