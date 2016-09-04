package com.ryanwelch.weather.injector.components;

import com.ryanwelch.weather.injector.modules.NetModule;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

//@Singleton
//@Component(dependencies = ApplicationComponent.class, modules = NetModule.class)
//public interface NetComponent {
//
//    OkHttpClient getOkHTTP();
//
//    Retrofit getRetrofit();
//
//}
