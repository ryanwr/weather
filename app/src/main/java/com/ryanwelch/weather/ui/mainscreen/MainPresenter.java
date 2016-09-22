package com.ryanwelch.weather.ui.mainscreen;

import android.util.Log;

import com.ryanwelch.weather.domain.interactors.DeletePlaceFactory;
import com.ryanwelch.weather.domain.interactors.GetCurrentWeatherFactory;
import com.ryanwelch.weather.domain.interactors.Interactor;
import com.ryanwelch.weather.injector.scopes.ActivityScope;
import com.ryanwelch.weather.domain.models.CurrentWeather;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

@ActivityScope
public class MainPresenter implements MainContract.Presenter {

    private final static String TAG = "MainPresenter";

    private MainContract.View mView;
    private boolean isEmpty = true;
    private boolean hasLoaded = false;

    @Inject GetCurrentWeatherFactory mGetCurrentWeatherFactory;
    @Inject DeletePlaceFactory mDeletePlaceFactory;

    @Inject
    public MainPresenter() {}

    @Override
    public void setView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void resume() {
        // Only show weather loading when the app is started,
        // when resuming from detail view etc. show refresh
        loadData(hasLoaded);
        hasLoaded = true;
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
    }

    private void loadData(boolean isRefreshing) {
        if(isRefreshing) mView.showRefreshing();
        else mView.showLoading();

        Interactor interactor = mGetCurrentWeatherFactory.get();
        interactor.execute(new Subscriber() {
            @Override
            public void onCompleted() {
                if(isRefreshing) mView.hideRefreshing();
                else mView.hideLoading();

                if(isEmpty) mView.showEmpty();
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error loading current weather: " + e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                List<CurrentWeather> data = (List<CurrentWeather>) o;
                if(data.isEmpty()) {
                    isEmpty = true;
                } else {
                    isEmpty = false;
                    mView.hideEmpty();
                    mView.showWeather(data);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        // Load data, use refresh anim
        loadData(true);
    }

    @Override
    public void onItemDismiss(CurrentWeather weather) {
        Interactor interactor = mDeletePlaceFactory.get(weather.place);
        interactor.execute(new Subscriber() {
            @Override
            public void onCompleted() {
                if(mView.getAdapterItemCount() == 0) {
                    isEmpty = true;
                    mView.showEmpty();
                }
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
    public void onItemSelected(CurrentWeather weather, WeatherListAdapter.WeatherItemViewHolder viewHolder) {
        mView.showDetail(weather, viewHolder);
    }
}
