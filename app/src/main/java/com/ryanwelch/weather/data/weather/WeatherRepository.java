package com.ryanwelch.weather.data.weather;

import com.ryanwelch.weather.domain.models.CurrentWeather;
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
    public Observable<CurrentWeather> getCurrentWeather(Place place) {
        return mWeatherLocalDataSource.getCurrentWeather(place)
                .filter((v) -> {
                    return v != null && new Date().before(new Date(v.updateTime.getTime() + EXPIRE_TIME));
                })
                .switchIfEmpty(
                        mWeatherRemoteDataSource.getCurrentWeather(place)
                                .doOnNext((p) -> {
                                    Timber.d("REMOTE: Loaded from remote, saving into db..");
                                    mWeatherLocalDataSource.setCurrentWeather(p).subscribe(
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
