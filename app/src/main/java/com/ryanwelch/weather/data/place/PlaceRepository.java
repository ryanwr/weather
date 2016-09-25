package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import rx.Observable;

public class PlaceRepository implements PlaceDataSource {

    private PlaceMemoryDataSource mPlaceMemoryDataSource;
    private PlaceSharedPreferencesDataSource mPlaceSharedPreferencesDataSource;

    public PlaceRepository(PlaceMemoryDataSource placeMemoryDataSource,
                           PlaceSharedPreferencesDataSource placeSharedPreferencesDataSource) {
        mPlaceMemoryDataSource = placeMemoryDataSource;
        mPlaceSharedPreferencesDataSource = placeSharedPreferencesDataSource;
    }

    @Override
    public Observable<List<Place>> getPlaces() {
        return mPlaceMemoryDataSource.getPlaces()
                .filter(v -> !v.isEmpty())
                .switchIfEmpty(
                        mPlaceSharedPreferencesDataSource.getPlaces()
                                .doOnNext(mPlaceMemoryDataSource::setPlaces)
                );
    }

    @Override
    public Observable<Void> setPlaces(List<Place> places) {
        return mPlaceMemoryDataSource.setPlaces(places);
    }

    @Override
    public Observable<Void> addPlace(Place place) {
        return mPlaceMemoryDataSource.addPlace(place).doOnCompleted(() -> {
            mPlaceMemoryDataSource.getPlaces()
                    .subscribe(mPlaceSharedPreferencesDataSource::setPlaces);
        });
    }

    @Override
    public Observable<Void> removePlace(Place place) {
        return mPlaceMemoryDataSource.removePlace(place).doOnCompleted(() -> {
            mPlaceMemoryDataSource.getPlaces()
                    .subscribe(mPlaceSharedPreferencesDataSource::setPlaces);
        });
    }

}
