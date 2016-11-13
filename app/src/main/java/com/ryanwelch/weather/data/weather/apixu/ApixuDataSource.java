/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather.apixu;

import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;
import com.ryanwelch.weather.domain.models.WeatherData;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class ApixuDataSource implements WeatherRemoteDataSource {

    public static final String APIXU_BASE_URL = "http://api.apixu.com/v1/";
    public static final String APIXU_API_KEY = BuildConfig.APIXU_API_TOKEN;

    private WeatherService mWeatherService;

    public ApixuDataSource(Retrofit retrofit) {
        mWeatherService = retrofit.create(WeatherService.class);
    }

    @Override
    public Observable<Weather> getWeather(Place place) {
//        return Observable.zip(
//                mWeatherService.getCurrentWeather(place.getLatitude() + ", " + place.getLongitude()),
//                mWeatherService.getForecast(place.getLatitude() + ", " + place.getLongitude(), 5),
//                this::transform);
        return mWeatherService.getForecast(place.getLatitude() + ", " + place.getLongitude(), 5)
                .map(this::transform);
    }

    private Weather transform(ApixuForecastResponse forecastResponse) {
        if(forecastResponse == null) return null;

        Weather weather = new Weather();
        weather.place = forecastResponse.place;
        weather.updateTime = new Date();
        weather.temperatureC = forecastResponse.current.tempC;
        weather.feelsLikeC = forecastResponse.current.feelsLikeC;
        weather.pressure = forecastResponse.current.pressureMb;
        weather.humidity = forecastResponse.current.humidity;
        weather.windSpeed = forecastResponse.current.windKph;
        weather.windDegree = forecastResponse.current.windDegree;
        weather.windDirection = forecastResponse.current.windDir;
        weather.isDay = forecastResponse.current.isDay != 0;
        weather.precipitation = forecastResponse.current.precipMm;
        weather.snow = 0;
        weather.cloud = forecastResponse.current.cloud;
        weather.sunriseTime = new Date();
        weather.sunsetTime = new Date();
        weather.weatherCondition = mapWeatherCondition(forecastResponse.current.condition.code);

        weather.daily = new ArrayList<>();
        for(ApixuForecastResponse.ForecastDay day : forecastResponse.forecast.daily) {
            WeatherData weatherData = new WeatherData();
            weatherData.time = new Date(day.dateEpoch);
            weatherData.temperatureC = day.day.avgTempC;
            weatherData.weatherCondition = mapWeatherCondition(day.day.condition.code);
            weatherData.place = forecastResponse.place;
            weather.daily.add(weatherData);
        }

        return weather;
    }

    private WeatherCondition mapWeatherCondition(int code) {
        switch (code) {
            case 1000:
                return WeatherCondition.SUNNY;
            case 1003:
                return WeatherCondition.PARTLY_CLOUDY;
            case 1006:
                return WeatherCondition.CLOUDY;
            case 1009:
                return WeatherCondition.OVERCAST;
            case 1030:
                return WeatherCondition.MIST;
            case 1063:
                return WeatherCondition.CHANCE_OF_RAIN;
            case 1066:
                return WeatherCondition.CHANCE_OF_SNOW;
            case 1069:
                return WeatherCondition.SLEET_SHOWERS;
            case 1072:
            case 1168:
            case 1171:
                return WeatherCondition.FREEZING_DRIZZLE;
            case 1087:
                return WeatherCondition.CHANCE_OF_THUNDERSTORM;
            case 1117:
                return WeatherCondition.BLIZZARD;
            case 1135:
            case 1147:
                return WeatherCondition.FOG;
            case 1150:
            case 1153:
                return WeatherCondition.LIGHT_DRIZZLE;
            case 1180:
            case 1183:
                return WeatherCondition.LIGHT_RAIN;
            case 1186:
            case 1189:
            case 1192:
            case 1195:
                return WeatherCondition.RAIN;
            case 1198:
            case 1201:
                return WeatherCondition.FREEZING_RAIN;
            case 1204:
            case 1207:
            case 1237:
            case 1261:
            case 1264:
                return WeatherCondition.SLEET;
            case 1210:
            case 1213:
                return WeatherCondition.LIGHT_SNOW;
            case 1114:
            case 1216:
            case 1219:
            case 1222:
            case 1225:
                return WeatherCondition.SNOW;
            case 1240:
                return WeatherCondition.SCATTERED_SHOWERS;
            case 1243:
                return WeatherCondition.SHOWERS;
            case 1246:
                return WeatherCondition.RAIN;
            case 1249:
            case 1252:
                return WeatherCondition.SLEET_SHOWERS;
            case 1255:
            case 1258:
                return WeatherCondition.SNOW_SHOWERS;
            case 1273:
                return WeatherCondition.STORM;
            case 1276:
                return  WeatherCondition.THUNDERSTORMS;
            case 1279:
            case 1282:
                return WeatherCondition.SNOW_THUNDERSTORMS;

            default:
                return WeatherCondition.SUNNY;
        }
    }

    interface WeatherService {
        @GET("current.json")
        Observable<ApixuWeatherResponse> getCurrentWeather(@Query("q") String query);

        @GET("forecast.json")
        Observable<ApixuForecastResponse> getForecast(@Query("q") String query, @Query("days") int days);
    }

}