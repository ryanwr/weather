package com.ryanwelch.weather.ui.mainscreen;

import com.ryanwelch.weather.domain.interactors.AddPlaceFactory;
import com.ryanwelch.weather.domain.interactors.DeletePlaceFactory;
import com.ryanwelch.weather.domain.interactors.GetCurrentWeatherFactory;
import com.ryanwelch.weather.domain.interactors.Interactor;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.injector.scopes.ActivityScope;
import com.ryanwelch.weather.domain.models.Weather;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import timber.log.Timber;

@ActivityScope
public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;
    private boolean isEmpty = true;
    private boolean hasLoaded = false;

    @Inject GetCurrentWeatherFactory mGetCurrentWeatherFactory;
    @Inject DeletePlaceFactory mDeletePlaceFactory;
    @Inject AddPlaceFactory mAddPlaceFactory;

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
                Timber.d("Error loading current weather: %s", e.getMessage());
                e.printStackTrace();

                if(isRefreshing) mView.hideRefreshing();
                else mView.hideLoading();

                if(isEmpty) mView.showEmpty();

                mView.showFailedLoadNotification();
            }

            @Override
            public void onNext(Object o) {
                List<Weather> data = (List<Weather>) o;
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
    public void onUndoDismiss(Place place) {
        Interactor interactor = mAddPlaceFactory.get(place);
        interactor.execute(new Subscriber() {
            @Override
            public void onCompleted() {
                onRefresh();
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
    public void onItemDismiss(Weather weather) {
        Interactor interactor = mDeletePlaceFactory.get(weather.place);
        interactor.execute(new Subscriber() {
            @Override
            public void onCompleted() {
                if(mView.getAdapterItemCount() == 0) {
                    isEmpty = true;
                    mView.showEmpty();
                }

                mView.showDismissNotification(weather.place);
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
    public void onItemSelected(Weather weather, WeatherListAdapter.WeatherItemViewHolder viewHolder) {
        mView.showDetail(weather, viewHolder);
    }
}
