package com.ryanwelch.weather.ui.mainscreen;

import com.ryanwelch.weather.domain.interactors.GetCurrentWeatherInteractor;
import com.ryanwelch.weather.injector.scopes.ActivityScope;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscriber;

@ActivityScope
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    @Inject GetCurrentWeatherInteractor mGetCurrentWeatherInteractor;

    @Inject
    public MainPresenter() {

    }

    @Override
    public void setView(MainContract.View view) {
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

    public void loadData() {
        mView.showLoading();
        mGetCurrentWeatherInteractor.execute(new Place("London", "Greater London", "United Kingdom", 51.4, -0.11), new Subscriber() {
            @Override
            public void onCompleted() {
                mView.hideLoading();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {
                ArrayList<CurrentWeather> places = new ArrayList<>();
                places.add((CurrentWeather) o);
                mView.showWeather(places);
            }
        });
    }

    public void addItem() {
//        WeatherApplication.getWeatherProvider().getCurrentWeather(new ResponseCallback<CurrentWeather>() {
//            @Override
//            public void onSuccess(CurrentWeather data) {
//                mWeatherListItems.add(data);
//                mWeatherListAdapter.notifyDataSetChanged();
//                Log.v("WeatherListAdapter", "Received data");
//            }
//
//            @Override
//            public void onFailure(String error) {
//                Log.e("WeatherListAdapter", error);
//            }
//        }, place);
    }

    @Override
    public void onRefresh() {

    }
}
