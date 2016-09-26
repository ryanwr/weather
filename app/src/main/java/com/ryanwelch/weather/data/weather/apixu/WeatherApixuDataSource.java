/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.weather.apixu;

import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;

import java.util.Date;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import timber.log.Timber;

public class WeatherApixuDataSource implements WeatherRemoteDataSource {

    private static final String TAG = "WeatherApixuDataSource";

    private WeatherService mWeatherService;

    @Inject
    public WeatherApixuDataSource(Retrofit retrofit) {
        mWeatherService = retrofit.create(WeatherService.class);
    }

    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        return mWeatherService.getCurrentWeather(place.getLatitude() + ", " + place.getLongitude())
                .map(this::transform);
    }

    private CurrentWeather transform(ApixuWeatherResponse weatherResponse) {
        CurrentWeather currentWeather = null;

        if(weatherResponse != null) {
            currentWeather = new CurrentWeather();
            currentWeather.place = weatherResponse.place;
            //currentWeather.updateTime = new Date(weatherResponse.current.lastUpdatedEpoch * 1000);
            currentWeather.updateTime = new Date();
            currentWeather.temperatureC = weatherResponse.current.tempC;
            currentWeather.feelsLikeC = weatherResponse.current.feelsLikeC;
            currentWeather.pressure = weatherResponse.current.pressureMb;
            currentWeather.humidity = weatherResponse.current.humidity;
            currentWeather.windSpeed = weatherResponse.current.windKph;
            currentWeather.windDegree = weatherResponse.current.windDegree;
            currentWeather.windDirection = weatherResponse.current.windDir;
            currentWeather.isDay = weatherResponse.current.isDay != 0;
            currentWeather.precipitation = weatherResponse.current.precipMm;
            currentWeather.snow = 0;
            currentWeather.cloud = weatherResponse.current.cloud;
            currentWeather.sunriseTime = new Date();
            currentWeather.sunsetTime = new Date();

            switch (weatherResponse.current.condition.code) {
                case 1000:
                    currentWeather.weatherCondition = WeatherCondition.SUNNY;
                    break;
                case 1003:
                    currentWeather.weatherCondition = WeatherCondition.PARTLY_CLOUDY;
                    break;
                case 1006:
                    currentWeather.weatherCondition = WeatherCondition.CLOUDY;
                    break;
                case 1009:
                    currentWeather.weatherCondition = WeatherCondition.OVERCAST;
                    break;
                case 1030:
                    currentWeather.weatherCondition = WeatherCondition.MIST;
                    break;
                case 1063:
                    currentWeather.weatherCondition = WeatherCondition.CHANCE_OF_RAIN;
                    break;
                case 1066:
                    currentWeather.weatherCondition = WeatherCondition.CHANCE_OF_SNOW;
                    break;
                case 1069:
                    currentWeather.weatherCondition = WeatherCondition.SLEET_SHOWERS;
                    break;
                case 1072:
                case 1168:
                case 1171:
                    currentWeather.weatherCondition = WeatherCondition.FREEZING_DRIZZLE;
                    break;
                case 1087:
                    currentWeather.weatherCondition = WeatherCondition.CHANCE_OF_THUNDERSTORM;
                    break;
                case 1117:
                    currentWeather.weatherCondition = WeatherCondition.BLIZZARD;
                    break;
                case 1135:
                case 1147:
                    currentWeather.weatherCondition = WeatherCondition.FOG;
                    break;
                case 1150:
                case 1153:
                    currentWeather.weatherCondition = WeatherCondition.LIGHT_DRIZZLE;
                    break;
                case 1180:
                case 1183:
                    currentWeather.weatherCondition = WeatherCondition.LIGHT_RAIN;
                    break;
                case 1186:
                case 1189:
                case 1192:
                case 1195:
                    currentWeather.weatherCondition = WeatherCondition.RAIN;
                    break;
                case 1198:
                case 1201:
                    currentWeather.weatherCondition = WeatherCondition.FREEZING_RAIN;
                    break;
                case 1204:
                case 1207:
                case 1237:
                case 1261:
                case 1264:
                    currentWeather.weatherCondition = WeatherCondition.SLEET;
                    break;
                case 1210:
                case 1213:
                    currentWeather.weatherCondition = WeatherCondition.LIGHT_SNOW;
                    break;
                case 1114:
                case 1216:
                case 1219:
                case 1222:
                case 1225:
                    currentWeather.weatherCondition = WeatherCondition.SNOW;
                    break;
                case 1240:
                    currentWeather.weatherCondition = WeatherCondition.SCATTERED_SHOWERS;
                    break;
                case 1243:
                    currentWeather.weatherCondition = WeatherCondition.SHOWERS;
                    break;
                case 1246:
                    // Torrential rain shower
                    break;
                case 1249:
                case 1252:
                    currentWeather.weatherCondition = WeatherCondition.SLEET_SHOWERS;
                    break;
                case 1255:
                case 1258:
                    currentWeather.weatherCondition = WeatherCondition.SNOW_SHOWERS;
                    break;
                case 1273:
                    currentWeather.weatherCondition = WeatherCondition.STORM;
                    break;
                case 1276:
                    currentWeather.weatherCondition = WeatherCondition.THUNDERSTORMS;
                    break;
                case 1279:
                case 1282:
                    currentWeather.weatherCondition = WeatherCondition.SNOW_THUNDERSTORMS;
                    break;

                default:
                    currentWeather.weatherCondition = WeatherCondition.SUNNY;
            }
        }

        return currentWeather;
    }

    public interface WeatherService {
        @GET("current.json")
        Observable<ApixuWeatherResponse> getCurrentWeather(@Query("q") String query);
    }

}