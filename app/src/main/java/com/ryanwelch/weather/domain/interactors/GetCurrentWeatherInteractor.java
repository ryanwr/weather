package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.data.weather.WeatherRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;

import rx.Observable;

public class GetCurrentWeatherInteractor extends Interactor {

    private final PlaceRepository mPlaceRepository;
    private final WeatherRepository mWeatherRepository;

    public GetCurrentWeatherInteractor(WeatherRepository weatherRepository,
                                       PlaceRepository placeRepository,
                                       ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mWeatherRepository = weatherRepository;
        mPlaceRepository = placeRepository;
    }

    @Override
    protected Observable run() {
        return mPlaceRepository.getPlaces()
                .flatMapIterable(ids -> ids)
                .flatMap(mWeatherRepository::getCurrentWeather)
                .toList();
    }
}
