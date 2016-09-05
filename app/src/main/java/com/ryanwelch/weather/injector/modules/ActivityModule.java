package com.ryanwelch.weather.injector.modules;

import android.app.Activity;

import com.ryanwelch.weather.injector.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @ActivityScope
    Activity activity() {
        return mActivity;
    }

}
