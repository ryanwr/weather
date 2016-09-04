package com.ryanwelch.weather.injector.modules;

import android.app.Activity;

import com.ryanwelch.weather.injector.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @PerActivity
    Activity activity() {
        return mActivity;
    }

}
