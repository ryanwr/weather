/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.WeatherCondition;
import com.ryanwelch.weather.domain.models.WeatherData;
import com.ryanwelch.weather.ui.BaseFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailForecastFragment extends BaseFragment implements DetailForecastContract.View {

    private static final String EXTRA_DATA = "DetailForecastData";

    public static DetailForecastFragment newInstance(Weather weather) {
        DetailForecastFragment fragment = new DetailForecastFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DATA, weather);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Inject DetailForecastPresenter mDetailForecastPresenter;

    @BindView(R.id.forecast_recycler_view) RecyclerView mForecastRecyclerView;

    private ForecastListAdapter mForecastListAdapter;

    public DetailForecastFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mDetailForecastPresenter.setData(bundle.getParcelable(EXTRA_DATA));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_forecast, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setupRecyclerView();
        mDetailForecastPresenter.setView(this);
    }

    private void setupRecyclerView() {
        // Setup adapter
        mForecastListAdapter = new ForecastListAdapter(getActivity(), new ArrayList<>());

        // Layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        // Setup recycler view
        mForecastRecyclerView.setHasFixedSize(true);
        mForecastRecyclerView.setAdapter(mForecastListAdapter);
        mForecastRecyclerView.setLayoutManager(layoutManager);
//        mForecastRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
//                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));
    }

    @Override
    public void showData(List<WeatherData> data) {
        mForecastListAdapter.replaceData(data);
    }


    @Override
    public void onResume() {
        super.onResume();
        this.mDetailForecastPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mDetailForecastPresenter.pause();
    }

    @Override
    public void onDestroy() {
        this.mDetailForecastPresenter.destroy();
        super.onDestroy();
    }

}
