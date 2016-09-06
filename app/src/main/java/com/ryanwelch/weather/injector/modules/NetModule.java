package com.ryanwelch.weather.injector.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.search.SearchProvider;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    private static final String SEARCH_BASE_URL = "http://api.apixu.com/v1/";
    private static final String SEARCH_API_KEY = BuildConfig.APIXU_API_TOKEN;

    public NetModule() {}

    @Provides
    @ApplicationScope
    // Application reference must come from ApplicationModule.class
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

//    @Provides
//    @ApplicationScope
//    Cache provideOkHttpCache(Application application) {
//        int cacheSize = 10 * 1024 * 1024; // 10 MiB
//        Cache cache = new Cache(application.getCacheDir(), cacheSize);
//        return cache;
//    }

    @Provides
    @ApplicationScope
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    private OkHttpClient createOkHTTPClient(String keyParam, String key) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor((chain) -> {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder().addQueryParameter(keyParam, key).build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .build();
        return client;
    }

    @Provides
    @Named("search")
    @ApplicationScope
    Retrofit provideSearchRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(SEARCH_BASE_URL)
                .client(createOkHTTPClient("key", SEARCH_API_KEY))
                .build();
        return retrofit;
    }

    @Provides
    @ApplicationScope
    SearchProvider provideSearchProvider(@Named("search") Retrofit retrofit) {
        return new SearchProvider(retrofit);
    }
}