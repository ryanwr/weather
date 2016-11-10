package com.ryanwelch.weather.ui.mainscreen;

import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.ui.BasePresenter;

import java.util.List;

public class MainContract {

    public interface View {
        void showEmpty();

        void hideEmpty();

        void showRefreshing();

        void hideRefreshing();

        void showLoading();

        void hideLoading();

        void showFailedLoadNotification();

        void showDismissNotification(Place place);

        void showWeather(List<Weather> weatherList);

        void showDetail(Weather weather, WeatherListAdapter.WeatherItemViewHolder viewHolder);

        int getAdapterItemCount();
    }

    public interface Presenter extends BasePresenter<View> {
        void onRefresh();

        void onUndoDismiss(Place place);

        void onItemDismiss(Weather weather);

        void onItemSelected(Weather weather, WeatherListAdapter.WeatherItemViewHolder viewHolder);
    }

}
