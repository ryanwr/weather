package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.Observable;

public class PlaceMemoryDataSource implements PlaceDataSource {

    private final List<Place> mPlaces = new ArrayList<>();

    @Override
    public Observable<List<Place>> getPlaces() {
        return Observable.just(mPlaces);
    }

    @Override
    public Completable setPlaces(List<Place> places) {
        return Completable.complete().doOnSubscribe((s) -> {
            mPlaces.clear();
            mPlaces.addAll(places);
        });
    }

    @Override
    public Completable addPlace(Place place) {
        return Completable.complete().doOnSubscribe((s) -> {
            if(!mPlaces.contains(place)) mPlaces.add(place);
        });
    }

    @Override
    public Completable removePlace(Place place) {
        return Completable.complete().doOnSubscribe((s) -> {
            if(mPlaces.contains(place)) mPlaces.remove(place);
        });
    }
}
