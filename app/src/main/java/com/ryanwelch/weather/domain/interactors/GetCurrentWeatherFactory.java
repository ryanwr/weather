package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.data.weather.WeatherRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;

import javax.inject.Inject;

import dagger.Lazy;

public class GetCurrentWeatherFactory {

    private final Lazy<PlaceRepository> mPlaceRepository;
    private final Lazy<WeatherRepository> mWeatherRepository;
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;

    @Inject
    public GetCurrentWeatherFactory(Lazy<WeatherRepository> weatherRepository,
                                    Lazy<PlaceRepository> placeRepository,
                                    ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        mPlaceRepository = placeRepository;
        mWeatherRepository = weatherRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    public GetCurrentWeatherInteractor get() {
        return new GetCurrentWeatherInteractor(mWeatherRepository.get(), mPlaceRepository.get(), mThreadExecutor, mPostExecutionThread);
    }

}
