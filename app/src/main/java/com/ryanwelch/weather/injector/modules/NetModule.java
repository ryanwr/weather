package com.ryanwelch.weather.injector.modules;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.data.search.SearchRemoteDataSource;
import com.ryanwelch.weather.data.search.SearchRepository;
import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.data.weather.WeatherRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;
import com.ryanwelch.weather.domain.executor.UIThread;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetModule {

    private static final String APIXU_BASE_URL = "http://api.apixu.com/v1/";
    private static final String APIXU_API_KEY = BuildConfig.APIXU_API_TOKEN;

    public NetModule() {}

    @Provides
    @ApplicationScope
    ThreadExecutor provideThreadExecutor() {
        return new ThreadExecutor();
    }

    @Provides
    @ApplicationScope
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
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
    @Named("apixu")
    @ApplicationScope
    Retrofit provideApixuRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(APIXU_BASE_URL)
                .client(createOkHTTPClient("key", APIXU_API_KEY))
                .build();
        return retrofit;
    }

    @Provides
    @ApplicationScope
    SearchRepository provideSearchRepository(@Named("apixu") Retrofit retrofit) {
        return new SearchRepository(new SearchRemoteDataSource(retrofit));
    }

    @Provides
    @ApplicationScope
    WeatherRepository provideWeatherRepository(@Named("apixu") Retrofit retrofit) {
        return new WeatherRepository(new WeatherRemoteDataSource(retrofit));
    }
}