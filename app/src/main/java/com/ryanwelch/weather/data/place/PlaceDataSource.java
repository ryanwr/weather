package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import rx.Completable;
import rx.Observable;

public interface PlaceDataSource {

    Observable<List<Place>> getPlaces();

    Completable setPlaces(List<Place> places);

    Completable addPlace(Place place);

    Completable removePlace(Place place);
}
