package com.ryanwelch.weather.ui.mainscreen;

import com.ryanwelch.weather.domain.models.CurrentWeather;
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

        void showFailedToLoad();

        void showWeather(List<CurrentWeather> weatherList);

        void showDetail(CurrentWeather weather, WeatherListAdapter.WeatherItemViewHolder viewHolder);

        int getAdapterItemCount();
    }

    public interface Presenter extends BasePresenter<View> {
        void onRefresh();

        void onItemDismiss(CurrentWeather weather);

        void onItemSelected(CurrentWeather weather, WeatherListAdapter.WeatherItemViewHolder viewHolder);
    }

}
