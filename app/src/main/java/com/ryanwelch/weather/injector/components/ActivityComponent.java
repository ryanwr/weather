package com.ryanwelch.weather.injector.components;

import android.app.Activity;

import com.ryanwelch.weather.injector.modules.ActivityModule;
import com.ryanwelch.weather.injector.scopes.ActivityScope;
import com.ryanwelch.weather.ui.detailscreen.DetailForecastFragment;
import com.ryanwelch.weather.ui.detailscreen.DetailFragment;
import com.ryanwelch.weather.ui.mainscreen.MainFragment;
import com.ryanwelch.weather.ui.searchscreen.SearchFragment;
import com.ryanwelch.weather.ui.settingsscreen.SettingsFragment;

import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent extends ApplicationComponent {

    void inject(SearchFragment searchFragment);
    void inject(MainFragment mainFragment);
    void inject(DetailFragment detailFragment);
    void inject(DetailForecastFragment detailForecastFragment);
    void inject(SettingsFragment settingsFragment);

    //Exposed to sub-graphs.
    Activity activity();
}
