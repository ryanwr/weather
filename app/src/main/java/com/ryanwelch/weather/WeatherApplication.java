package com.ryanwelch.weather;

import android.app.Application;
import android.location.Location;
import android.support.annotation.Nullable;

import com.ryanwelch.weather.data.LocationProvider;
import com.ryanwelch.weather.data.WeatherProvider;
import com.ryanwelch.weather.data.WeatherProviderDebugImpl;
import com.ryanwelch.weather.data.WeatherProviderImpl;

public class WeatherApplication extends Application {

    private static WeatherProvider mWeatherProvider;
    private static LocationProvider mLocationProvider;

    private static Location mCurrentLocation;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            mWeatherProvider = new WeatherProviderDebugImpl();
        } else {
            mWeatherProvider = new WeatherProviderImpl();
        }

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
