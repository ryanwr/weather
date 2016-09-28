/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import com.ryanwelch.weather.injector.components.ActivityComponent;
import com.ryanwelch.weather.injector.components.ApplicationComponent;
import com.ryanwelch.weather.injector.modules.ActivityModule;
import com.ryanwelch.weather.injector.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface DetailComponent extends ActivityComponent {
    void inject(DetailFragment detailFragment);
    void inject(DetailForecastFragment detailForecastFragment);
}
