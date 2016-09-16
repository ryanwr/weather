package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

public class PlaceMemoryDataSource implements PlaceDataSource {

    private final List<Place> mPlaces = new ArrayList<>();

    @Override
    public Observable<List<Place>> getPlaces() {
        return Observable.just(mPlaces);
    }

    @Override
    public Observable<Void> setPlaces(List<Place> places) {
        mPlaces.clear();
        mPlaces.addAll(places);
        return Observable.empty();
    }

    @Override
    public Observable<Void> addPlace(Place place) {
        if(!mPlaces.contains(place)) mPlaces.add(place);
        return Observable.empty();
    }

    @Override
    public Observable<Void> removePlace(Place place) {
        if(mPlaces.contains(place)) mPlaces.remove(place);
        return Observable.empty();
    }
}
