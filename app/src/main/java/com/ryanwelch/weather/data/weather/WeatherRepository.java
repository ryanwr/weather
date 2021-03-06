package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;

import java.util.Date;

import rx.Observable;
import timber.log.Timber;

public class WeatherRepository implements WeatherDataSource {

    // 10 Minutes expire time
    private static final long EXPIRE_TIME = 10 * 60 * 1000;

    private WeatherRemoteDataSource mWeatherRemoteDataSource;
    private WeatherLocalDataSource mWeatherLocalDataSource;

    public WeatherRepository(WeatherRemoteDataSource weatherRemoteDataSource,
                             WeatherLocalDataSource weatherLocalDataSource) {
        mWeatherRemoteDataSource = weatherRemoteDataSource;
        mWeatherLocalDataSource = weatherLocalDataSource;
    }

    @Override
    public Observable<Weather> getWeather(Place place) {
        // TODO: Fallback to whatever local data we have (i.e. no filtering) if network not available
        return mWeatherLocalDataSource.getWeather(place)
                .filter(
                        (v) -> v != null && new Date().before(new Date(v.updateTime.getTime() + EXPIRE_TIME))
                )
                .switchIfEmpty(
                        mWeatherRemoteDataSource.getWeather(place)
                                .doOnNext((weather) -> {
                                    Timber.d("REMOTE: Loaded from remote, saving into db..");
                                    mWeatherLocalDataSource.setWeather(weather).subscribe(
                                            (v) -> Timber.d("Saved into db"),
                                            (e) -> {
                                                Timber.e(e.getMessage());
                                                e.printStackTrace();
                                            },
                                            () -> Timber.d("Saved into db")
                                    );
                                })
                                .doOnError(e -> Timber.e("Error %s", e.getMessage()))
                );
    }
}
