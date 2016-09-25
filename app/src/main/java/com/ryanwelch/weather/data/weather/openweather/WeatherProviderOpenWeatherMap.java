//package com.ryanwelch.weather.data.weather;
//
//import com.ryanwelch.weather.BuildConfig;
//import com.ryanwelch.weather.domain.models.CurrentWeather;
//import com.ryanwelch.weather.domain.models.Place;
//import com.ryanwelch.weather.domain.models.WeatherCondition;
//import com.ryanwelch.weather.data.weather.openweather.OpenWeatherResponse;
//
//import java.io.IOException;
//import java.util.Date;
//
//import okhttp3.HttpUrl;
//import okhttp3.Interceptor;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//import retrofit2.*;
//import retrofit2.Callback;
//import retrofit2.converter.gson.GsonConverterFactory;
//import retrofit2.http.GET;
//import retrofit2.http.Query;
//
//public class WeatherProviderOpenWeatherMap implements WeatherProvider {
//
//    private static final String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";
//    private static final String WEATHER_API_KEY = BuildConfig.OPEN_WEATHER_API_TOKEN;
//
//    private WeatherService weatherService;
//
//    public WeatherProviderOpenWeatherMap() {
//        Interceptor requestInterceptor = new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request();
//                HttpUrl url = request.url().newBuilder().addQueryParameter("apikey", WEATHER_API_KEY).build();
//                request = request.newBuilder().url(url).build();
//
//                return chain.proceed(request);
//            }
//        };
//
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(requestInterceptor).build();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(WEATHER_BASE_URL)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        weatherService = retrofit.create(WeatherService.class);
//    }
//
//    public void getCurrentWeather(final ResponseCallback<CurrentWeather> callback, Place place) {
//        Call<OpenWeatherResponse> response = weatherService.getCurrentWeather(place.getLatitude(), place.getLongitude());
//
//        response.enqueue(new Callback<OpenWeatherResponse>() {
//            @Override
//            public void onResponse(Call<OpenWeatherResponse> call, retrofit2.Response<OpenWeatherResponse> response) {
//                if(response.isSuccessful()) {
//                    OpenWeatherResponse data = response.body();
//
//                    CurrentWeather reply = new CurrentWeather();
//                    //reply.location = data.coord;
//                    reply.updateTime = new Date(data.dt * 1000);
//                    //reply.cityName = data.cityName;
//                    //reply.countryCode = data.sys.country;
//                    reply.sunriseTime = new Date(data.sys.sunrise * 1000);
//                    reply.sunsetTime = new Date(data.sys.sunset * 1000);
//                    if(data.rain != null) {
//                        reply.rain = data.rain.volume;
//                    } else {
//                        reply.rain = 0;
//                    }
//                    if(data.snow != null) {
//                        reply.snow = data.snow.volume;
//                    } else {
//                        reply.snow = 0;
//                    }
//                    reply.cloudPercent = data.clouds.coverage;
//                    //reply.wind = data.wind;
//                    reply.temperature = data.main.temperature;
//                    if(data.main.groundLevel != null) {
//                        reply.pressure = data.main.groundLevel;
//                    } else {
//                        reply.pressure = data.main.pressure;
//                    }
//                    reply.humidity = data.main.humidity;
//
//
//                    // Default
//                    reply.weatherCondition = WeatherCondition.SUNNY;
//                    reply.weatherDescription = "Sunny";
//                    //reply.weatherIcon
//
//                    if(data.weather.size() > 0) {
//                        Integer weatherId = data.weather.get(0).id;
//                        reply.weatherDescription = data.weather.get(0).description;
//
//                        switch (weatherId) {
//                            case 200:
//                            case 230:
//                                reply.weatherCondition = WeatherCondition.CHANCE_OF_STORM;
//                                break;
//                            case 201:
//                            case 202:
//                            case 231:
//                            case 232:
//                                reply.weatherCondition = WeatherCondition.STORM;
//                                break;
//                            case 210:
//                                reply.weatherCondition = WeatherCondition.CHANCE_OF_THUNDERSTORM;
//                                break;
//                            case 211:
//                            case 212:
//                            case 221:
//                                reply.weatherCondition = WeatherCondition.THUNDERSTORMS;
//                                break;
//
//                            case 300:
//                            case 310:
//                                reply.weatherCondition = WeatherCondition.LIGHT_DRIZZLE;
//                                break;
//                            case 301:
//                            case 302:
//                            case 311:
//                            case 312:
//                                reply.weatherCondition = WeatherCondition.DRIZZLE;
//                                break;
//                            case 313:
//                            case 314:
//                            case 321:
//                                reply.weatherCondition = WeatherCondition.DRIZZLE_SHOWERS;
//                                break;
//
//                            case 500:
//                                reply.weatherCondition = WeatherCondition.CHANCE_OF_RAIN;
//                                break;
//                            case 501:
//                                reply.weatherCondition = WeatherCondition.LIGHT_RAIN;
//                                break;
//                            case 502:
//                            case 503:
//                            case 504:
//                                reply.weatherCondition = WeatherCondition.RAIN;
//                                break;
//                            case 511:
//                                reply.weatherCondition = WeatherCondition.FREEZING_RAIN;
//                                break;
//                            case 520:
//                                reply.weatherCondition = WeatherCondition.SCATTERED_SHOWERS;
//                                break;
//                            case 521:
//                            case 522:
//                            case 531:
//                                reply.weatherCondition = WeatherCondition.SHOWERS;
//                                break;
//
//                            case 600:
//                                reply.weatherCondition = WeatherCondition.LIGHT_SNOW;
//                                break;
//                            case 601:
//                            case 602:
//                            case 615:
//                            case 616:
//                                reply.weatherCondition = WeatherCondition.SNOW;
//                                break;
//                            case 620:
//                            case 621:
//                            case 622:
//                                reply.weatherCondition = WeatherCondition.SNOW_SHOWERS;
//                                break;
//                            case 611:
//                                reply.weatherCondition = WeatherCondition.SLEET;
//                                break;
//                            case 612:
//                                reply.weatherCondition = WeatherCondition.SLEET_SHOWERS;
//                                break;
//
//                            case 701:
//                                reply.weatherCondition = WeatherCondition.MIST;
//                                break;
//                            case 711:
//                            case 762:
//                                reply.weatherCondition = WeatherCondition.SMOKE;
//                                break;
//                            case 721:
//                                reply.weatherCondition = WeatherCondition.HAZE;
//                                break;
//                            case 741:
//                                reply.weatherCondition = WeatherCondition.FOG;
//                                break;
//                            case 731:
//                            case 751:
//                            case 761:
//                                reply.weatherCondition = WeatherCondition.DUST;
//                                break;
//
//                            // 771, 781
//
//                            case 800:
//                                reply.weatherCondition = WeatherCondition.SUNNY;
//                                break;
//                            case 801:
//                                reply.weatherCondition = WeatherCondition.MOSTLY_SUNNY;
//                                break;
//                            case 802:
//                                reply.weatherCondition = WeatherCondition.PARTLY_CLOUDY;
//                                break;
//                            case 803:
//                                reply.weatherCondition = WeatherCondition.MOSTLY_CLOUDY;
//                                break;
//                            case 804:
//                                reply.weatherCondition = WeatherCondition.CLOUDY;
//                                break;
//
//                            // 9xx
//
//                            default:
//                                reply.weatherCondition = WeatherCondition.SUNNY;
//                        }
//                    }
//
//                    callback.onSuccess(reply);
//                } else {
//                    callback.onFailure(response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OpenWeatherResponse> call, Throwable t) {
//                callback.onFailure(t.getMessage());
//            }
//        });
//
//    }
//
//    public interface WeatherService {
//
//        @GET("weather?units=metric")
//        Call<OpenWeatherResponse> getCurrentWeather(@Query("lat") double latitude,
//                                                    @Query("lon") double longitude);
//
//
//    }
//
//}
