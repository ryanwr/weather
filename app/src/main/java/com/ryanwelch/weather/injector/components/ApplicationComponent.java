package com.ryanwelch.weather.injector.components;

import android.content.Context;

import com.google.gson.Gson;
import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.data.search.SearchRepository;
import com.ryanwelch.weather.data.weather.WeatherRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;
import com.ryanwelch.weather.injector.modules.ApplicationModule;
import com.ryanwelch.weather.injector.modules.DebugModule;
import com.ryanwelch.weather.injector.modules.NetModule;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;
import com.ryanwelch.weather.ui.BaseActivity;
import com.ryanwelch.weather.ui.BaseFragment;
import com.squareup.leakcanary.RefWatcher;

import dagger.Component;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;

@ApplicationScope
@Component(modules = {ApplicationModule.class, NetModule.class, DebugModule.class})
public interface ApplicationComponent {

    void inject(BaseActivity activity);
    void inject(BaseFragment fragment);

    //Exposes Application Context to any component which depends on this
    Context getContext();
    ThreadExecutor getThreadExecutor();
    PostExecutionThread getPostExecutionThread();
    Gson getGson();
    ReactiveLocationProvider getLocationProvider();
    SearchRepository getSearchRepository();
    WeatherRepository getWeatherRepository();
    PlaceRepository getPlaceRepository();
    RefWatcher getRefWatcher();
}
