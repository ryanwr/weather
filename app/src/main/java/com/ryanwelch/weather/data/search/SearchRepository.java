package com.ryanwelch.weather.data.search;

import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import rx.Observable;

public class SearchRepository implements SearchDataSource {

    private SearchRemoteDataSource mSearchRemoteDataSource;

    public SearchRepository(SearchRemoteDataSource searchRemoteDataSource) {
        mSearchRemoteDataSource = searchRemoteDataSource;
    }

    public Observable<List<Place>> suggestionsList(String query) {
        return mSearchRemoteDataSource.suggestionsList(query);
    }


}
