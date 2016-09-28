package com.ryanwelch.weather.data.place;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ryanwelch.weather.domain.models.Place;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import timber.log.Timber;

public class PlaceSharedPreferencesDataSource implements PlaceDataSource {

    private static final String SP_PLACE_ENTITIES = "PLACE_ENTITIES";
    private final SharedPreferences mSharedPreferences;
    private final Gson mGson;

    @Inject
    public PlaceSharedPreferencesDataSource(SharedPreferences sharedPreferences,
                                            Gson gson) {
        this.mSharedPreferences = sharedPreferences;
        this.mGson = gson;
    }

    @Override
    public Observable<List<Place>> getPlaces() {
        Timber.v("getPlaces");
        Type type = new TypeToken<List<Place>>(){}.getType();
        List<Place> places = mGson.fromJson(mSharedPreferences.getString(SP_PLACE_ENTITIES, "[]"), type);
        return Observable.just(places);
    }

    @Override
    public Observable<Void> setPlaces(List<Place> places) {
        Timber.v("setPlaces");
        mSharedPreferences.edit().putString(SP_PLACE_ENTITIES, mGson.toJson(places)).apply();
        return Observable.empty();
    }

    @Override
    public Observable<Void> addPlace(Place place) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Observable<Void> removePlace(Place place) {
        throw new UnsupportedOperationException();
    }
}
