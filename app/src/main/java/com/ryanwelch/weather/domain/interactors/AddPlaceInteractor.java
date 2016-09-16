package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;
import com.ryanwelch.weather.domain.models.Place;

import rx.Observable;

public class AddPlaceInteractor extends Interactor {

    private final PlaceRepository mPlaceRepository;
    private final Place mPlace;

    public AddPlaceInteractor(PlaceRepository placeRepository,
                              ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread,
                              Place place) {
        super(threadExecutor, postExecutionThread);
        mPlaceRepository = placeRepository;
        mPlace = place;
    }

    @Override
    public Observable run() {
        return mPlaceRepository.addPlace(mPlace);
    }

}
