package com.ryanwelch.weather.data.search;

import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import rx.Observable;

public interface SearchDataSource {

    Observable<List<Place>> suggestionsList(String query);
}
