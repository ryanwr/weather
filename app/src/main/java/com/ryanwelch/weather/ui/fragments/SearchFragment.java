package com.ryanwelch.weather.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.WeatherApplication;
import com.ryanwelch.weather.data.search.SearchProvider;
import com.ryanwelch.weather.models.Place;
import com.ryanwelch.weather.models.SearchSuggestion;
import com.ryanwelch.weather.ui.activities.SearchActivity;
import com.ryanwelch.weather.ui.components.FloatingSearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";

    @BindView(R.id.floating_search_view) FloatingSearchView mFloatingSearchView;

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        setupFloatingSearch();
    }

    private void setupFloatingSearch() {

        mFloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mFloatingSearchView.clearSuggestions();
                } else {
                    mFloatingSearchView.showProgress();

                    WeatherApplication.getSearchProvider().findSuggestions(newQuery, new SearchProvider.OnFindListener(){

                        @Override
                        public void onResults(List<Place> results) {
                            mFloatingSearchView.swapSuggestions(results);
                            mFloatingSearchView.hideProgress();
                        }
                    });
                }
            }
        });

        mFloatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                ((SearchActivity) getActivity()).returnResult((Place) searchSuggestion);
            }
        });

        mFloatingSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeAction() {
                ((SearchActivity) getActivity()).returnCancel();
            }
        });

        mFloatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mFloatingSearchView.swapSuggestions(WeatherApplication.getSearchProvider().getHistory(3));
                // TODO: Current location
            }

            @Override
            public void onFocusCleared() {
            }
        });
    }

}
