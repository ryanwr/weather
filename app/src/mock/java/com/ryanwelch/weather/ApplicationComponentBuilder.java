/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather;

import com.ryanwelch.weather.data.injector.modules.MockNetModule;
import com.ryanwelch.weather.injector.components.DaggerApplicationComponent;
import com.ryanwelch.weather.injector.modules.ApplicationModule;

public class ApplicationComponentBuilder {

    public static DaggerApplicationComponent.Builder build(WeatherApplication application) {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(application))
                .netModule(new MockNetModule());
    }

}
