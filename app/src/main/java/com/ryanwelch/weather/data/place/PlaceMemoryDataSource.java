package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import java.util.Collections;
import java.util.List;

import rx.Observable;

public class PlaceMemoryDataSource implements PlaceDataSource {

    List<Place> mPlaces = Collections.emptyList();

    @Override
    public Observable<List<Place>> getPlaces() {
        return Observable.just(mPlaces);
    }

    @Override
    public Observable<Void> setPlaces(List<Place> places) {
        mPlaces = places;
        return Observable.empty();
    }

    @Override
    public Observable<Void> addPlace(Place place) {
        if(!mPlaces.contains(place)) mPlaces.add(place);
        return Observable.empty();
    }
}
