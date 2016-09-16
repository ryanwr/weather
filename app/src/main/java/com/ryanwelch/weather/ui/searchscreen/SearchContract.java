package com.ryanwelch.weather.ui.searchscreen;

import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.ui.BasePresenter;

import java.util.List;

public class SearchContract {

    public interface View {
        void showSuggestions(List<Place> placeList);

        void showLoading();

        void hideLoading();
    }

    public interface Presenter extends BasePresenter<View> {
        void loadHistory();

        void loadQuery(String query);

        void onSelected(Place place);
    }

}
