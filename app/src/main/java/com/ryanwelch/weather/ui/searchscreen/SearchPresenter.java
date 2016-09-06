package com.ryanwelch.weather.ui.searchscreen;

import com.ryanwelch.weather.WeatherApplication;
import com.ryanwelch.weather.data.search.SearchProvider;
import com.ryanwelch.weather.models.Place;

import java.util.List;

import javax.inject.Inject;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View mView;

    @Inject SearchProvider mSearchProvider;

    @Inject
    public SearchPresenter() {

    }

    @Override
    public void setView(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void loadHistory() {
        mView.showLoading();

        // On loaded
        //mView.showSuggestions(getHistory());
        mView.hideLoading();
    }

    @Override
    public void loadQuery(String query) {
        mView.showLoading();

        // On loaded
//        WeatherApplication.getSearchProvider().findSuggestions(query, new SearchProvider.OnFindListener(){
//            @Override
//            public void onResults(List<Place> results) {
//                mView.showSuggestions(results);
//                mView.hideLoading();
//            }
//        });
    }

    @Override
    public void addPlace(Place place) {

    }
}
