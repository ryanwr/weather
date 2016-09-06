package com.ryanwelch.weather.injector.components;

import com.ryanwelch.weather.injector.modules.ActivityModule;
import com.ryanwelch.weather.injector.modules.MainModule;
import com.ryanwelch.weather.injector.scopes.ActivityScope;
import com.ryanwelch.weather.ui.mainscreen.MainFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, MainModule.class})
public interface MainComponent extends ActivityComponent {
    void inject(MainFragment mainFragment);
}
