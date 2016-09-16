package com.ryanwelch.weather.ui.searchscreen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.SearchSuggestion;
import com.ryanwelch.weather.ui.BaseFragment;
import com.ryanwelch.weather.ui.components.FloatingSearchView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends BaseFragment implements SearchContract.View,
        FloatingSearchView.OnFocusChangeListener, FloatingSearchView.OnHomeActionClickListener,
        FloatingSearchView.OnSearchListener, FloatingSearchView.OnQueryChangeListener {

    private static final String TAG = "SearchFragment";

    @Inject SearchPresenter mSearchPresenter;

    @BindView(R.id.floating_search_view) FloatingSearchView mFloatingSearchView;

    private SearchListener mListener;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(SearchComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupFloatingSearch();
        mSearchPresenter.setView(this);
        mSearchPresenter.loadHistory();
    }

    private void setupFloatingSearch() {
        mFloatingSearchView.setOnQueryChangeListener(this);
        mFloatingSearchView.setOnSearchListener(this);
        mFloatingSearchView.setOnHomeActionClickListener(this);
        mFloatingSearchView.setOnFocusChangeListener(this);
    }


    @Override
    public void showSuggestions(List<Place> placeList) {
        mFloatingSearchView.swapSuggestions(placeList);
    }

    @Override
    public void showLoading() {
        mFloatingSearchView.showProgress();
    }

    @Override
    public void hideLoading() {
        mFloatingSearchView.hideProgress();
    }

    @Override
    public void onFocus() {

    }

    @Override
    public void onFocusCleared() {

    }

    @Override
    public void onHomeAction() {
        mListener.finishActivity();
    }

    @Override
    public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
        mSearchPresenter.onSelected((Place) searchSuggestion);
        mListener.finishActivity();
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        if (!oldQuery.equals("") && newQuery.equals("")) {
            mFloatingSearchView.clearSuggestions();
        } else {
            mSearchPresenter.loadQuery(newQuery);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchListener) {
            this.mListener = (SearchListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interface for listening to MainFragment events
     */
    public interface SearchListener {
        void finishActivity();
    }
}
