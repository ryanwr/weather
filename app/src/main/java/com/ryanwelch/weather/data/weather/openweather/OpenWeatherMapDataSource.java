package com.ryanwelch.weather.data.weather.openweather;

import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.data.weather.WeatherRemoteDataSource;
import com.ryanwelch.weather.domain.models.Weather;
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
    public Observable<Weather> getCurrentWeather(Place place) {
        return mWeatherService.getCurrentWeather(place.getLatitude(), place.getLongitude())
                .map((weather) -> this.transform(weather, place));
    }

    private Weather transform(OpenWeatherResponse weatherResponse, Place place) {
        if(weatherResponse == null) return null;

        Weather weather = new Weather();
        weather.place = place;
        //weather.updateTime = new Date(weatherResponse.current.lastUpdatedEpoch * 1000);
        weather.updateTime = new Date();
        weather.temperatureC = weatherResponse.main.temperature;
        // TODO: No feels like? :(
        weather.feelsLikeC = weatherResponse.main.temperature;
        weather.pressure = weatherResponse.main.pressure;
        weather.humidity = weatherResponse.main.humidity;
        weather.windSpeed = weatherResponse.wind.speed;
        weather.windDegree = weatherResponse.wind.deg;
        weather.windDirection = "N/A";
        weather.isDay = true;
        weather.precipitation = weatherResponse.rain.volume.doubleValue();
        weather.snow = weatherResponse.snow.volume;
        weather.cloud = weatherResponse.clouds.coverage;
        weather.sunriseTime = new Date(weatherResponse.sys.sunrise * 1000);
        weather.sunsetTime = new Date(weatherResponse.sys.sunset * 1000);
        weather.weatherCondition = WeatherCondition.SUNNY;

        if(weatherResponse.weather.size() > 0) {
            Integer weatherId = weatherResponse.weather.get(0).id;

            switch (weatherId) {
                case 200:
                case 230:
                    weather.weatherCondition = WeatherCondition.CHANCE_OF_STORM;
                    break;
                case 201:
                case 202:
                case 231:
                case 232:
                    weather.weatherCondition = WeatherCondition.STORM;
                    break;
                case 210:
                    weather.weatherCondition = WeatherCondition.CHANCE_OF_THUNDERSTORM;
                    break;
                case 211:
                case 212:
                case 221:
                    weather.weatherCondition = WeatherCondition.THUNDERSTORMS;
                    break;

                case 300:
                case 310:
                    weather.weatherCondition = WeatherCondition.LIGHT_DRIZZLE;
                    break;
                case 301:
                case 302:
                case 311:
                case 312:
                    weather.weatherCondition = WeatherCondition.DRIZZLE;
                    break;
                case 313:
                case 314:
                case 321:
                    weather.weatherCondition = WeatherCondition.DRIZZLE_SHOWERS;
                    break;

                case 500:
                    weather.weatherCondition = WeatherCondition.CHANCE_OF_RAIN;
                    break;
                case 501:
                    weather.weatherCondition = WeatherCondition.LIGHT_RAIN;
                    break;
                case 502:
                case 503:
                case 504:
                    weather.weatherCondition = WeatherCondition.RAIN;
                    break;
                case 511:
                    weather.weatherCondition = WeatherCondition.FREEZING_RAIN;
                    break;
                case 520:
                    weather.weatherCondition = WeatherCondition.SCATTERED_SHOWERS;
                    break;
                case 521:
                case 522:
                case 531:
                    weather.weatherCondition = WeatherCondition.SHOWERS;
                    break;

                case 600:
                    weather.weatherCondition = WeatherCondition.LIGHT_SNOW;
                    break;
                case 601:
                case 602:
                case 615:
                case 616:
                    weather.weatherCondition = WeatherCondition.SNOW;
                    break;
                case 620:
                case 621:
                case 622:
                    weather.weatherCondition = WeatherCondition.SNOW_SHOWERS;
                    break;
                case 611:
                    weather.weatherCondition = WeatherCondition.SLEET;
                    break;
                case 612:
                    weather.weatherCondition = WeatherCondition.SLEET_SHOWERS;
                    break;

                case 701:
                    weather.weatherCondition = WeatherCondition.MIST;
                    break;
                case 711:
                case 762:
                    weather.weatherCondition = WeatherCondition.SMOKE;
                    break;
                case 721:
                    weather.weatherCondition = WeatherCondition.HAZE;
                    break;
                case 741:
                    weather.weatherCondition = WeatherCondition.FOG;
                    break;
                case 731:
                case 751:
                case 761:
                    weather.weatherCondition = WeatherCondition.DUST;
                    break;

                // 771, 781

                case 800:
                    weather.weatherCondition = WeatherCondition.SUNNY;
                    break;
                case 801:
                    weather.weatherCondition = WeatherCondition.MOSTLY_SUNNY;
                    break;
                case 802:
                    weather.weatherCondition = WeatherCondition.PARTLY_CLOUDY;
                    break;
                case 803:
                    weather.weatherCondition = WeatherCondition.MOSTLY_CLOUDY;
                    break;
                case 804:
                    weather.weatherCondition = WeatherCondition.CLOUDY;
                    break;

                // 9xx

                default:
                    weather.weatherCondition = WeatherCondition.SUNNY;
            }
        }

        return weather;

    }

    interface WeatherService {
        @GET("weather?units=metric")
        Observable<OpenWeatherResponse> getCurrentWeather(@Query("lat") double latitude,
                                                          @Query("lon") double longitude);
    }

}
