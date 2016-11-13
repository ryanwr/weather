package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import rx.Completable;
import rx.Observable;

public class PlaceRepository implements PlaceDataSource {

    private PlaceMemoryDataSource mPlaceMemoryDataSource;
    private PlaceLocalDataSource mPlaceLocalDataSource;

    public PlaceRepository(PlaceMemoryDataSource placeMemoryDataSource,
                           PlaceLocalDataSource placeLocalDataSource) {
        mPlaceMemoryDataSource = placeMemoryDataSource;
        mPlaceLocalDataSource = placeLocalDataSource;
    }

    @Override
    public Observable<List<Place>> getPlaces() {
        return mPlaceMemoryDataSource
                .getPlaces()
                .filter(v -> !v.isEmpty())
                .switchIfEmpty(
                        mPlaceLocalDataSource
                                .getPlaces()
                                .doOnNext((places) -> mPlaceMemoryDataSource.setPlaces(places).subscribe())
                );
    }

    @Override
    public Completable setPlaces(List<Place> places) {
        return mPlaceMemoryDataSource.setPlaces(places);
    }

    @Override
    public Completable addPlace(Place place) {
        return mPlaceMemoryDataSource
                .addPlace(place)
                .doOnSubscribe((s) -> {
                    mPlaceLocalDataSource.addPlace(place).subscribe();
                });
    }

    @Override
    public Completable removePlace(Place place) {
        return mPlaceMemoryDataSource
                .removePlace(place)
                .doOnSubscribe((s) -> {
                    mPlaceLocalDataSource.removePlace(place).subscribe();
                });
    }

}
