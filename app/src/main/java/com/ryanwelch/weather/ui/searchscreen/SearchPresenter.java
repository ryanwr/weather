package com.ryanwelch.weather.ui.searchscreen;

import android.util.Log;

import com.ryanwelch.weather.domain.interactors.GetSearchSuggestionInteractor;
import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

public class SearchPresenter implements SearchContract.Presenter {

    private static final String TAG = "SearchPresenter";

    private SearchContract.View mView;

    @Inject GetSearchSuggestionInteractor mGetSearchSuggestionInteractor;

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
       // mView.showLoading();

        // On loaded
        //mView.showSuggestions(getHistory());
        //mView.hideLoading();
    }

    @Override
    public void loadQuery(String query) {
        mView.showLoading();
        mGetSearchSuggestionInteractor.execute(query, new Subscriber() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                mView.hideLoading();
                Log.v(TAG, e.getLocalizedMessage());
            }

            @Override
            public void onNext(Object o) {
                mView.showSuggestions((List<Place>) o);
            }
        });
    }

    @Override
    public void addPlace(Place place) {

    }
}
