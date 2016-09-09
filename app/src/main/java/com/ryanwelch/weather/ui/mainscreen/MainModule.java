package com.ryanwelch.weather.ui.mainscreen;

import com.ryanwelch.weather.injector.scopes.ActivityScope;
import com.ryanwelch.weather.ui.mainscreen.MainPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    public MainModule() {}

    @Provides
    @ActivityScope
    MainPresenter providesMainPresenter() {
        return new MainPresenter();
    }
}
