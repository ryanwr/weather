package com.ryanwelch.weather.data.weather.openweather;

import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;

import java.util.Date;

import retrofit2.*;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class OpenWeatherMapDataSource implements WeatherRemoteDataSource {

    public static final String OPEN_WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String OPEN_WEATHER_API_KEY = BuildConfig.OPEN_WEATHER_API_TOKEN;

    private WeatherService mWeatherService;

    public OpenWeatherMapDataSource(Retrofit retrofit) {
        mWeatherService = retrofit.create(WeatherService.class);
    }

    @Override
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        return mWeatherService.getCurrentWeather(place.getLatitude(), place.getLongitude())
                .map((weather) -> this.transform(weather, place));
    }

    private CurrentWeather transform(OpenWeatherResponse weatherResponse, Place place) {
        if(weatherResponse == null) return null;

        CurrentWeather currentWeather = new CurrentWeather();
        currentWeather.place = place;
        //currentWeather.updateTime = new Date(weatherResponse.current.lastUpdatedEpoch * 1000);
        currentWeather.updateTime = new Date();
        currentWeather.temperatureC = weatherResponse.main.temperature;
        // TODO: No feels like? :(
        currentWeather.feelsLikeC = weatherResponse.main.temperature;
        currentWeather.pressure = weatherResponse.main.pressure;
        currentWeather.humidity = weatherResponse.main.humidity;
        currentWeather.windSpeed = weatherResponse.wind.speed;
        currentWeather.windDegree = weatherResponse.wind.deg;
        currentWeather.windDirection = "N/A";
        currentWeather.isDay = true;
        currentWeather.precipitation = weatherResponse.rain.volume.doubleValue();
        currentWeather.snow = weatherResponse.snow.volume;
        currentWeather.cloud = weatherResponse.clouds.coverage;
        currentWeather.sunriseTime = new Date(weatherResponse.sys.sunrise * 1000);
        currentWeather.sunsetTime = new Date(weatherResponse.sys.sunset * 1000);
        currentWeather.weatherCondition = WeatherCondition.SUNNY;

        if(weatherResponse.weather.size() > 0) {
            Integer weatherId = weatherResponse.weather.get(0).id;

            switch (weatherId) {
                case 200:
                case 230:
                    currentWeather.weatherCondition = WeatherCondition.CHANCE_OF_STORM;
                    break;
                case 201:
                case 202:
                case 231:
                case 232:
                    currentWeather.weatherCondition = WeatherCondition.STORM;
                    break;
                case 210:
                    currentWeather.weatherCondition = WeatherCondition.CHANCE_OF_THUNDERSTORM;
                    break;
                case 211:
                case 212:
                case 221:
                    currentWeather.weatherCondition = WeatherCondition.THUNDERSTORMS;
                    break;

                case 300:
                case 310:
                    currentWeather.weatherCondition = WeatherCondition.LIGHT_DRIZZLE;
                    break;
                case 301:
                case 302:
                case 311:
                case 312:
                    currentWeather.weatherCondition = WeatherCondition.DRIZZLE;
                    break;
                case 313:
                case 314:
                case 321:
                    currentWeather.weatherCondition = WeatherCondition.DRIZZLE_SHOWERS;
                    break;

                case 500:
                    currentWeather.weatherCondition = WeatherCondition.CHANCE_OF_RAIN;
                    break;
                case 501:
                    currentWeather.weatherCondition = WeatherCondition.LIGHT_RAIN;
                    break;
                case 502:
                case 503:
                case 504:
                    currentWeather.weatherCondition = WeatherCondition.RAIN;
                    break;
                case 511:
                    currentWeather.weatherCondition = WeatherCondition.FREEZING_RAIN;
                    break;
                case 520:
                    currentWeather.weatherCondition = WeatherCondition.SCATTERED_SHOWERS;
                    break;
                case 521:
                case 522:
                case 531:
                    currentWeather.weatherCondition = WeatherCondition.SHOWERS;
                    break;

                case 600:
                    currentWeather.weatherCondition = WeatherCondition.LIGHT_SNOW;
                    break;
                case 601:
                case 602:
                case 615:
                case 616:
                    currentWeather.weatherCondition = WeatherCondition.SNOW;
                    break;
                case 620:
                case 621:
                case 622:
                    currentWeather.weatherCondition = WeatherCondition.SNOW_SHOWERS;
                    break;
                case 611:
                    currentWeather.weatherCondition = WeatherCondition.SLEET;
                    break;
                case 612:
                    currentWeather.weatherCondition = WeatherCondition.SLEET_SHOWERS;
                    break;

                case 701:
                    currentWeather.weatherCondition = WeatherCondition.MIST;
                    break;
                case 711:
                case 762:
                    currentWeather.weatherCondition = WeatherCondition.SMOKE;
                    break;
                case 721:
                    currentWeather.weatherCondition = WeatherCondition.HAZE;
                    break;
                case 741:
                    currentWeather.weatherCondition = WeatherCondition.FOG;
                    break;
                case 731:
                case 751:
                case 761:
                    currentWeather.weatherCondition = WeatherCondition.DUST;
                    break;

                // 771, 781

                case 800:
                    currentWeather.weatherCondition = WeatherCondition.SUNNY;
                    break;
                case 801:
                    currentWeather.weatherCondition = WeatherCondition.MOSTLY_SUNNY;
                    break;
                case 802:
                    currentWeather.weatherCondition = WeatherCondition.PARTLY_CLOUDY;
                    break;
                case 803:
                    currentWeather.weatherCondition = WeatherCondition.MOSTLY_CLOUDY;
                    break;
                case 804:
                    currentWeather.weatherCondition = WeatherCondition.CLOUDY;
                    break;

                // 9xx

                default:
                    currentWeather.weatherCondition = WeatherCondition.SUNNY;
            }
        }

        return currentWeather;

    }

    interface WeatherService {
        @GET("weather?units=metric")
        Observable<OpenWeatherResponse> getCurrentWeather(@Query("lat") double latitude,
                                                          @Query("lon") double longitude);
    }

}
