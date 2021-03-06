package com.ryanwelch.weather.injector.modules;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.ryanwelch.weather.data.search.SearchRemoteDataSource;
import com.ryanwelch.weather.data.search.SearchRepository;
import com.ryanwelch.weather.data.weather.WeatherLocalDataSource;
import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.data.weather.apixu.ApixuDataSource;
import com.ryanwelch.weather.data.weather.WeatherRepository;
import com.ryanwelch.weather.data.weather.darksky.DarkSkyDataSource;
import com.ryanwelch.weather.data.weather.openweather.OpenWeatherMapDataSource;
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
import timber.log.Timber;

@Module
public class NetModule {

    public enum DataSource {
        APIXU("apixu"),
        DARK_SKY("dark_sky"),
        OPEN_WEATHER_MAP("open_weather_map");

        private String text;

        DataSource(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static DataSource fromString(String text) {
            if (text != null) {
                for (DataSource d : DataSource.values()) {
                    if (text.equalsIgnoreCase(d.text)) {
                        return d;
                    }
                }
            }
            throw new IllegalArgumentException("No constant with text " + text + " found");
        }
    }

    private final DataSource mDataSource;

    public NetModule(DataSource dataSource) {
        mDataSource = dataSource;
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
                .baseUrl(ApixuDataSource.APIXU_BASE_URL)
                .client(createOkHTTPClient("key", ApixuDataSource.APIXU_API_KEY))
                .build();
    }

    @Provides
    @Named("openweather")
    @ApplicationScope
    Retrofit provideOpenWeatherRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(OpenWeatherMapDataSource.OPEN_WEATHER_BASE_URL)
                .client(createOkHTTPClient("apikey", OpenWeatherMapDataSource.OPEN_WEATHER_API_KEY))
                .build();
    }

    @Provides
    @Named("darksky")
    @ApplicationScope
    Retrofit provideDarkSkyRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(DarkSkyDataSource.DARK_SKY_BASE_URL)
                .client(new OkHttpClient.Builder().build())
                .build();
    }

    @Provides
    @ApplicationScope
    WeatherRemoteDataSource provideWeatherRemoteDataSource(@Named("apixu") Retrofit apixuRetrofit,
                                                           @Named("openweather") Retrofit openweatherRetrofit,
                                                           @Named("darksky") Retrofit darkskyRetrofit) {
        Timber.i("Using %s data source.", mDataSource.toString());
        switch(mDataSource) {
            case APIXU:
                return new ApixuDataSource(apixuRetrofit);
            case OPEN_WEATHER_MAP:
                return new OpenWeatherMapDataSource(openweatherRetrofit);
            case DARK_SKY:
                return new DarkSkyDataSource(darkskyRetrofit);
            default:
                return null;
        }
    }

    @Provides
    @ApplicationScope
    WeatherRepository provideWeatherRepository(WeatherRemoteDataSource remoteDataSource,
                                               StorIOSQLite storIOSQLite) {
        return new WeatherRepository(remoteDataSource,
                new WeatherLocalDataSource(storIOSQLite));
    }

    @Provides
    @ApplicationScope
    SearchRepository provideSearchRepository(@Named("apixu") Retrofit retrofit) {
        return new SearchRepository(new SearchRemoteDataSource(retrofit));
    }
}