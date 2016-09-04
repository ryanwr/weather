package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.ResponseCallback;
import com.ryanwelch.weather.data.models.ApixuWeatherResponse;
import com.ryanwelch.weather.models.CurrentWeather;
import com.ryanwelch.weather.models.Place;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherProviderApixu implements WeatherProvider {

    private static final String TAG = "WeatherProviderApixu";

    private static final String WEATHER_BASE_URL = "http://api.apixu.com/v1/";
    private static final String WEATHER_API_KEY = BuildConfig.APIXU_API_TOKEN;

    private WeatherService weatherService;

    public WeatherProviderApixu() {
        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("key", WEATHER_API_KEY).build();
                request = request.newBuilder().url(url).build();

                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(requestInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherService = retrofit.create(WeatherService.class);
    }

    public void getCurrentWeather(final ResponseCallback<CurrentWeather> callback, Place palce) {
        Call<ApixuWeatherResponse> response = weatherService.getCurrentWeather("");

        response.enqueue(new Callback<ApixuWeatherResponse>() {
            @Override
            public void onResponse(Call<ApixuWeatherResponse> call, retrofit2.Response<ApixuWeatherResponse> response) {
                if(response.isSuccessful()) {
                    ApixuWeatherResponse data = response.body();

                    CurrentWeather reply = new CurrentWeather();

                    callback.onSuccess(reply);
                } else {
                    callback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(Call<ApixuWeatherResponse> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });

    }

    public interface WeatherService {

        @GET("current.json")
        Call<ApixuWeatherResponse> getCurrentWeather(@Query("q") String query);

    }

}