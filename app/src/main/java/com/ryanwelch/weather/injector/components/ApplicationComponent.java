package com.ryanwelch.weather.injector.components;

import android.content.Context;

import com.ryanwelch.weather.injector.modules.ApplicationModule;
import com.ryanwelch.weather.ui.activities.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity activity);

    //Exposes Application Context to any component which depends on this
    Context getContext();
}
