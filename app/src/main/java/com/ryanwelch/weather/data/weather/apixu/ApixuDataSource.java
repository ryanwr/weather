/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather.apixu;

import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;

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
    public Observable<Weather> getCurrentWeather(Place place) {
        return mWeatherService.getCurrentWeather(place.getLatitude() + ", " + place.getLongitude())
                .map(this::transform);
    }

    private Weather transform(ApixuWeatherResponse weatherResponse) {
        if(weatherResponse == null) return null;

        Weather weather = new Weather();
        weather.place = weatherResponse.place;
        //weather.updateTime = new Date(weatherResponse.current.lastUpdatedEpoch * 1000);
        weather.updateTime = new Date();
        weather.temperatureC = weatherResponse.current.tempC;
        weather.feelsLikeC = weatherResponse.current.feelsLikeC;
        weather.pressure = weatherResponse.current.pressureMb;
        weather.humidity = weatherResponse.current.humidity;
        weather.windSpeed = weatherResponse.current.windKph;
        weather.windDegree = weatherResponse.current.windDegree;
        weather.windDirection = weatherResponse.current.windDir;
        weather.isDay = weatherResponse.current.isDay != 0;
        weather.precipitation = weatherResponse.current.precipMm;
        weather.snow = 0;
        weather.cloud = weatherResponse.current.cloud;
        weather.sunriseTime = new Date();
        weather.sunsetTime = new Date();

        switch (weatherResponse.current.condition.code) {
            case 1000:
                weather.weatherCondition = WeatherCondition.SUNNY;
                break;
            case 1003:
                weather.weatherCondition = WeatherCondition.PARTLY_CLOUDY;
                break;
            case 1006:
                weather.weatherCondition = WeatherCondition.CLOUDY;
                break;
            case 1009:
                weather.weatherCondition = WeatherCondition.OVERCAST;
                break;
            case 1030:
                weather.weatherCondition = WeatherCondition.MIST;
                break;
            case 1063:
                weather.weatherCondition = WeatherCondition.CHANCE_OF_RAIN;
                break;
            case 1066:
                weather.weatherCondition = WeatherCondition.CHANCE_OF_SNOW;
                break;
            case 1069:
                weather.weatherCondition = WeatherCondition.SLEET_SHOWERS;
                break;
            case 1072:
            case 1168:
            case 1171:
                weather.weatherCondition = WeatherCondition.FREEZING_DRIZZLE;
                break;
            case 1087:
                weather.weatherCondition = WeatherCondition.CHANCE_OF_THUNDERSTORM;
                break;
            case 1117:
                weather.weatherCondition = WeatherCondition.BLIZZARD;
                break;
            case 1135:
            case 1147:
                weather.weatherCondition = WeatherCondition.FOG;
                break;
            case 1150:
            case 1153:
                weather.weatherCondition = WeatherCondition.LIGHT_DRIZZLE;
                break;
            case 1180:
            case 1183:
                weather.weatherCondition = WeatherCondition.LIGHT_RAIN;
                break;
            case 1186:
            case 1189:
            case 1192:
            case 1195:
                weather.weatherCondition = WeatherCondition.RAIN;
                break;
            case 1198:
            case 1201:
                weather.weatherCondition = WeatherCondition.FREEZING_RAIN;
                break;
            case 1204:
            case 1207:
            case 1237:
            case 1261:
            case 1264:
                weather.weatherCondition = WeatherCondition.SLEET;
                break;
            case 1210:
            case 1213:
                weather.weatherCondition = WeatherCondition.LIGHT_SNOW;
                break;
            case 1114:
            case 1216:
            case 1219:
            case 1222:
            case 1225:
                weather.weatherCondition = WeatherCondition.SNOW;
                break;
            case 1240:
                weather.weatherCondition = WeatherCondition.SCATTERED_SHOWERS;
                break;
            case 1243:
                weather.weatherCondition = WeatherCondition.SHOWERS;
                break;
            case 1246:
                // Torrential rain shower
                break;
            case 1249:
            case 1252:
                weather.weatherCondition = WeatherCondition.SLEET_SHOWERS;
                break;
            case 1255:
            case 1258:
                weather.weatherCondition = WeatherCondition.SNOW_SHOWERS;
                break;
            case 1273:
                weather.weatherCondition = WeatherCondition.STORM;
                break;
            case 1276:
                weather.weatherCondition = WeatherCondition.THUNDERSTORMS;
                break;
            case 1279:
            case 1282:
                weather.weatherCondition = WeatherCondition.SNOW_THUNDERSTORMS;
                break;

            default:
                weather.weatherCondition = WeatherCondition.SUNNY;
        }

        return weather;
    }

    interface WeatherService {
        @GET("current.json")
        Observable<ApixuWeatherResponse> getCurrentWeather(@Query("q") String query);
    }

}