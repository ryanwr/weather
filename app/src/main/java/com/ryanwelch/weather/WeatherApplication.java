package com.ryanwelch.weather;

import android.app.Application;
import android.content.Context;
import android.support.v7.preference.PreferenceManager;

import com.frogermcs.androiddevmetrics.AndroidDevMetrics;
import com.ryanwelch.weather.injector.components.ApplicationComponent;
import com.ryanwelch.weather.injector.components.DaggerApplicationComponent;
import com.ryanwelch.weather.injector.modules.ApplicationModule;
import com.ryanwelch.weather.injector.modules.DebugModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

public class WeatherApplication extends Application {

    private ApplicationComponent mApplicationComponent;
    private RefWatcher mRefWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("Application onCreate()");
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mRefWatcher = LeakCanary.install(this);

        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            AndroidDevMetrics.initWith(this);
        } else {
            //TODO: Timber.plant(); Crash reporting logger?
        }

        mApplicationComponent = prepareApplicationComponent().build();

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        Timber.i("Initialized app");
    }

    protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
        return ApplicationComponentBuilder.build(this)
                .debugModule(new DebugModule(mRefWatcher));
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public static WeatherApplication from(Context context) {
        return (WeatherApplication) context.getApplicationContext();
    }

}
