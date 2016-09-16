package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import rx.Observable;

public interface PlaceDataSource {

    Observable<List<Place>> getPlaces();

    Observable<Void> setPlaces(List<Place> places);

    Observable<Void> addPlace(Place place);

    Observable<Void> removePlace(Place place);
}
