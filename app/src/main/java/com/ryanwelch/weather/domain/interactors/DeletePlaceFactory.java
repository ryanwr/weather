package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;
import com.ryanwelch.weather.domain.models.Place;

import javax.inject.Inject;

import dagger.Lazy;

public class DeletePlaceFactory {

    private final Lazy<PlaceRepository> mPlaceRepository;
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;

    @Inject
    public DeletePlaceFactory(Lazy<PlaceRepository> placeRepository,
                              ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        mPlaceRepository = placeRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    public DeletePlaceInteractor get(Place place) {
        return new DeletePlaceInteractor(mPlaceRepository.get(), mThreadExecutor, mPostExecutionThread, place);
    }

}
