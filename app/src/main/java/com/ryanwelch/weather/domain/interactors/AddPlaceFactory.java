package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;
import com.ryanwelch.weather.domain.models.Place;

import javax.inject.Inject;

public class AddPlaceFactory {

    private final PlaceRepository mPlaceRepository;
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;

    @Inject
    public AddPlaceFactory(PlaceRepository placeRepository,
                              ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        mPlaceRepository = placeRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    public AddPlaceInteractor get(Place place) {
        return new AddPlaceInteractor(mPlaceRepository, mThreadExecutor, mPostExecutionThread, place);
    }

}
