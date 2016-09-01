package com.ryanwelch.weather;

import android.app.Application;
import android.location.Location;
import android.support.annotation.Nullable;

import com.ryanwelch.weather.data.location.LocationProvider;
import com.ryanwelch.weather.data.weather.WeatherProvider;
import com.ryanwelch.weather.data.weather.WeatherProviderDebugImpl;
import com.ryanwelch.weather.data.weather.WeatherProviderImpl;

public class WeatherApplication extends Application {

    private static WeatherProvider mWeatherProvider;
    private static LocationProvider mLocationProvider;

    private static Location mCurrentLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        //if (BuildConfig.DEBUG) {
        //    mWeatherProvider = new WeatherProviderDebugImpl();
        //} else {
            mWeatherProvider = new WeatherProviderImpl();
        //}

        mLocationProvider = new LocationProvider(this, new LocationProvider.LocationCallback() {
            @Override
            public void handleNewLocation(Location location) {
                mCurrentLocation = location;
            }
        });
    }


    public static WeatherProvider getWeatherProvider() {
        return mWeatherProvider;
    }

    public static LocationProvider getLocationProvider() {
        return mLocationProvider;
    }

    @Nullable
    public static Location getLocation() {
        return mCurrentLocation;
    }
}
