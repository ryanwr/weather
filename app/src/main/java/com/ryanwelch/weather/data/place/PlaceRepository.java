package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class PlaceRepository implements PlaceDataSource {

    private PlaceMemoryDataSource mPlaceMemoryDataSource;
    private PlaceSharedPreferencesDataSource mPlaceSharedPreferencesDataSource;

    @Inject
    public PlaceRepository(PlaceMemoryDataSource placeMemoryDataSource,
                           PlaceSharedPreferencesDataSource placeSharedPreferencesDataSource) {
        mPlaceMemoryDataSource = placeMemoryDataSource;
        mPlaceSharedPreferencesDataSource = placeSharedPreferencesDataSource;
    }

    @Override
    public Observable<List<Place>> getPlaces() {
        return Observable.concat(
                mPlaceMemoryDataSource.getPlaces(),
                mPlaceSharedPreferencesDataSource.getPlaces()
                        .doOnNext(mPlaceMemoryDataSource::setPlaces)
        ).first();
    }

    @Override
    public Observable<Void> setPlaces(List<Place> places) {
        return mPlaceMemoryDataSource.setPlaces(places);
    }

    @Override
    public Observable<Void> addPlace(Place place) {
        mPlaceMemoryDataSource.addPlace(place);
        mPlaceMemoryDataSource.getPlaces()
                .doOnNext(mPlaceSharedPreferencesDataSource::setPlaces)
                .subscribe();
        return null;
    }

}
