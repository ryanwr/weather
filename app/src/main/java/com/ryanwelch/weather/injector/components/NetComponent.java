package com.ryanwelch.weather.injector.components;

import com.ryanwelch.weather.injector.modules.NetModule;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

@ApplicationScope
@Subcomponent(modules = NetModule.class)
public interface NetComponent {

    OkHttpClient getOkHTTP();

    Retrofit getRetrofit();

}
