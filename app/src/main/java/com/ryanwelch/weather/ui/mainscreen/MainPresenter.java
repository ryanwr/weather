package com.ryanwelch.weather.ui.mainscreen;

import android.util.Log;
import android.view.View;

import com.ryanwelch.weather.domain.interactors.DeletePlaceFactory;
import com.ryanwelch.weather.domain.interactors.DeletePlaceInteractor;
import com.ryanwelch.weather.domain.interactors.GetCurrentWeatherFactory;
import com.ryanwelch.weather.domain.interactors.Interactor;
import com.ryanwelch.weather.injector.scopes.ActivityScope;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

@ActivityScope
public class MainPresenter implements MainContract.Presenter {

    private final static String TAG = "MainPresenter";

    private MainContract.View mView;

    @Inject GetCurrentWeatherFactory mGetCurrentWeatherFactory;
    @Inject DeletePlaceFactory mDeletePlaceFactory;

    @Inject
    public MainPresenter() {

    }

    @Override
    public void setView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void resume() {
        loadData();
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void loadData() {
        mView.showLoading();
        Interactor interactor = mGetCurrentWeatherFactory.get();
        interactor.execute(new Subscriber() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error loading current weather: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                mView.showWeather((List<CurrentWeather>) o);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void onItemDismiss(CurrentWeather weather) {
        Interactor interactor = mDeletePlaceFactory.get(weather.place);
        interactor.execute(new Subscriber() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Object o) {
            }
        });
    }

    @Override
    public void onItemSelected(CurrentWeather weather, View view) {
        mView.showDetail(weather.place, view);
    }
}
