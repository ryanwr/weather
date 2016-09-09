package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.weather.WeatherRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;
import com.ryanwelch.weather.domain.models.Place;

import javax.inject.Inject;

import rx.Subscriber;

public class GetCurrentWeatherInteractor extends Interactor {

    private final WeatherRepository mWeatherRepository;

    @Inject
    public GetCurrentWeatherInteractor(WeatherRepository weatherRepository,
                                       ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mWeatherRepository = weatherRepository;
    }

    public void execute(Place place, Subscriber subscriber) {
        super.execute(mWeatherRepository.currentWeather(place), subscriber);
    }

}
