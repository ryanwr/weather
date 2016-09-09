package com.ryanwelch.weather.ui.mainscreen;

import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.ui.BasePresenter;

import java.util.List;

public class MainContract {

    public interface View {
        void showLoading();

        void hideLoading();

        void showWeather(List<CurrentWeather> weatherList);
    }

    public interface Presenter extends BasePresenter<View> {
        void loadData();

        void onRefresh();
    }

}
