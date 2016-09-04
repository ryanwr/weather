package com.ryanwelch.weather.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.WeatherApplication;
import com.ryanwelch.weather.data.ResponseCallback;
import com.ryanwelch.weather.models.CurrentWeather;
import com.ryanwelch.weather.models.Place;
import com.ryanwelch.weather.presenters.MainContract;
import com.ryanwelch.weather.ui.adapters.WeatherListAdapter;
import com.ryanwelch.weather.ui.helpers.OnStartDragListener;
import com.ryanwelch.weather.ui.helpers.SimpleItemTouchHelperCallback;
import com.ryanwelch.weather.ui.helpers.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements MainContract.View, OnStartDragListener {

    private static final String TAG = "MainFragment";

    @BindView(R.id.weather_view) RecyclerView mRecyclerView;
    @BindView(R.id.weather_swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private WeatherListAdapter mWeatherListAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private final ArrayList<CurrentWeather> mWeatherListItems;

    public MainFragment() {
        mWeatherListItems = new ArrayList<>();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);

        mWeatherListAdapter = new WeatherListAdapter(getActivity(), mWeatherListItems, this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mWeatherListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mWeatherListAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

//        if(BuildConfig.DEBUG) {
//            addWeatherLocation(51.507351, -0.127758);
//            addWeatherLocation(51.507351, -0.127758);
//            addWeatherLocation(51.507351, -0.127758);
//        }
    }

    public void addWeatherLocation(Place place) {
        WeatherApplication.getWeatherProvider().getCurrentWeather(new ResponseCallback<CurrentWeather>() {
            @Override
            public void onSuccess(CurrentWeather data) {
                mWeatherListItems.add(data);
                mWeatherListAdapter.notifyDataSetChanged();
                Log.v("WeatherListAdapter", "Received data");
            }

            @Override
            public void onFailure(String error) {
                Log.e("WeatherListAdapter", error);
            }
        }, place);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
