package com.ryanwelch.weather.ui.mainscreen;

import com.ryanwelch.weather.injector.components.ActivityComponent;
import com.ryanwelch.weather.injector.components.ApplicationComponent;
import com.ryanwelch.weather.injector.modules.ActivityModule;
import com.ryanwelch.weather.injector.scopes.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class})
public interface MainComponent extends ActivityComponent {
    void inject(MainFragment mainFragment);
}
