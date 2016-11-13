/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.data.place;


import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.ryanwelch.weather.data.db.PlaceTable;
import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import rx.Completable;
import rx.Observable;

public class PlaceLocalDataSource implements PlaceDataSource {

    private StorIOSQLite mStorIOSQLite;

    public PlaceLocalDataSource(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
    }

    @Override
    public Observable<List<Place>> getPlaces() {
        return mStorIOSQLite
                .get()
                .listOfObjects(Place.class)
                .withQuery(PlaceTable.QUERY_ALL)
                .prepare()
                .asRxObservable()
                .take(1);

    }

    @Override
    public Completable setPlaces(List<Place> places) {
        return mStorIOSQLite
                .put()
                .objects(places)
                .prepare()
                .asRxObservable()
                .toCompletable();
    }

    @Override
    public Completable addPlace(Place place) {
        return mStorIOSQLite
                .put()
                .object(place)
                .prepare()
                .asRxObservable()
                .toCompletable();
    }

    @Override
    public Completable removePlace(Place place) {
        return mStorIOSQLite
                .delete()
                .object(place)
                .prepare()
                .asRxObservable()
                .toCompletable();
    }
}
