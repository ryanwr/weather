package com.ryanwelch.weather.injector.modules;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.db.CurrentWeatherTable;
import com.ryanwelch.weather.data.db.DbOpenHelper;
import com.ryanwelch.weather.data.search.SearchRemoteDataSource;
import com.ryanwelch.weather.data.search.SearchRepository;
import com.ryanwelch.weather.data.weather.WeatherLocalDataSource;
import com.ryanwelch.weather.data.weather.apixu.ApixuDataSource;
import com.ryanwelch.weather.data.weather.WeatherRepository;
import com.ryanwelch.weather.data.weather.darksky.DarkSkyDataSource;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;
import com.ryanwelch.weather.domain.executor.UIThread;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.PlaceSQLiteTypeMapping;
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
    private static final String OPEN_WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String OPEN_WEATHER_API_KEY = BuildConfig.OPEN_WEATHER_API_TOKEN;
    private static final String DARK_SKY_BASE_URL = "https://api.darksky.net/";
    private static final String DARK_SKY_API_KEY = BuildConfig.DARK_SKY_API_TOKEN;

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

    @Provides
    @ApplicationScope
    Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }

    private OkHttpClient createOkHTTPClient(String keyParam, String key) {
        return new OkHttpClient.Builder()
                .addInterceptor((chain) -> {
                    Request request = chain.request();
                    HttpUrl url = request.url().newBuilder().addQueryParameter(keyParam, key).build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .build();
    }

    @Provides
    @Named("apixu")
    @ApplicationScope
    Retrofit provideApixuRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(APIXU_BASE_URL)
                .client(createOkHTTPClient("key", APIXU_API_KEY))
                .build();
    }

    @Provides
    @Named("openweather")
    @ApplicationScope
    Retrofit provideOpenWeatherRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(OPEN_WEATHER_BASE_URL)
                .client(createOkHTTPClient("apikey", OPEN_WEATHER_API_KEY))
                .build();
    }

    @Provides
    @Named("darksky")
    @ApplicationScope
    Retrofit provideDarkSkyRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(DARK_SKY_BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .build();
    }

    @Provides
    @ApplicationScope
    SearchRepository provideSearchRepository(@Named("apixu") Retrofit retrofit) {
        return new SearchRepository(new SearchRemoteDataSource(retrofit));
    }

    @Provides
    @ApplicationScope
    WeatherRepository provideWeatherRepository(@Named("apixu") Retrofit retrofit, StorIOSQLite storIOSQLite) {
        return new WeatherRepository(new ApixuDataSource(retrofit),
                new WeatherLocalDataSource(storIOSQLite));
    }

    @Provides
    @ApplicationScope
    SQLiteOpenHelper provideSQLiteOpenHelper(Context context) {
        //context.deleteDatabase("weather_db");
        return new DbOpenHelper(context);
    }

    @Provides
    @ApplicationScope
    StorIOSQLite provideStorIO(SQLiteOpenHelper openHelper) {
        return DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(openHelper)
                .addTypeMapping(Place.class, new PlaceSQLiteTypeMapping())
                .addTypeMapping(CurrentWeather.class, SQLiteTypeMapping.<CurrentWeather>builder()
                        .putResolver(CurrentWeatherTable.PUT_RESOLVER)
                        .getResolver(CurrentWeatherTable.GET_RESOLVER)
                        .deleteResolver(CurrentWeatherTable.DELETE_RESOLVER)
                        .build())
                .build();
    }
}