package com.ryanwelch.weather.ui.searchscreen;

import com.ryanwelch.weather.injector.scopes.ActivityScope;
import com.ryanwelch.weather.ui.searchscreen.SearchPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SearchModule {

    public SearchModule() {}

    @Provides
    @ActivityScope
    SearchPresenter providesSearchPresenter() {
        return new SearchPresenter();
    }
}