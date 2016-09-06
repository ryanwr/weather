//package com.ryanwelch.weather.data.weather;
//
//import com.ryanwelch.weather.models.CurrentWeather;
//import com.ryanwelch.weather.models.Place;
//import com.ryanwelch.weather.models.WeatherCondition;
//
//import java.util.Date;
//
//public class WeatherProviderDebug implements WeatherProvider {
//
//    private int count = 0;
//
//    @Override
//    public void getCurrentWeather(ResponseCallback<CurrentWeather> callback, Place place) {
//        CurrentWeather reply = new CurrentWeather();
//        count++;
//        switch(count) {
//            case 1:
//                reply.updateTime = new Date();
//                reply.weatherCondition = WeatherCondition.RAIN;
//                reply.temperature = 17.0;
//                reply.place = new Place("London", "Essex", "United Kingdom");
//                break;
//            case 2:
//                reply.updateTime = new Date();
//                reply.weatherCondition = WeatherCondition.SUNNY;
//                reply.temperature = 23.0;
//                reply.place = new Place("New York", "", "United States");
//                break;
//            case 3:
//                reply.updateTime = new Date();
//                reply.weatherCondition = WeatherCondition.THUNDERSTORMS;
//                reply.temperature = 5.0;
//                reply.place = new Place("Alaska", "", "Canada");
//
//                count = 0;
//                break;
//        }
//        callback.onSuccess(reply);
//    }
//
//}
