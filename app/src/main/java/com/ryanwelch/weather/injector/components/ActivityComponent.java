package com.ryanwelch.weather.injector.components;

import android.app.Activity;

import com.ryanwelch.weather.injector.modules.ActivityModule;
import com.ryanwelch.weather.injector.scopes.PerActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    //Exposed to sub-graphs.
    Activity activity();
}
