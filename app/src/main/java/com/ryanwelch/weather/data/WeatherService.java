package com.ryanwelch.weather.data;

import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.models.responses.CurrentWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherService {

    @GET("/weather?units=metric")
    Call<CurrentWeatherResponse> getCurrentWeather(@Query("lon") double longitude,
                                                   @Query("lat") double latitude);

//    @GET("/forecast/daily?units=metric&cnt=7")
//    Call<Response> getForecast(@Query("lon") double longitude,
//                     @Query("lat") double latitude);

}
