package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.data.weather.WeatherRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;
import com.ryanwelch.weather.domain.models.Place;

import javax.inject.Inject;

public class GetCurrentWeatherFactory {

    private final PlaceRepository mPlaceRepository;
    private final WeatherRepository mWeatherRepository;
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;

    @Inject
    public GetCurrentWeatherFactory(WeatherRepository weatherRepository,
                                    PlaceRepository placeRepository,
                                    ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        mPlaceRepository = placeRepository;
        mWeatherRepository = weatherRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    public GetCurrentWeatherInteractor get() {
        return new GetCurrentWeatherInteractor(mWeatherRepository, mPlaceRepository, mThreadExecutor, mPostExecutionThread);
    }

}
