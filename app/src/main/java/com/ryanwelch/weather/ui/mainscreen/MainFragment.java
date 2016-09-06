package com.ryanwelch.weather.ui.mainscreen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.injector.components.MainComponent;
import com.ryanwelch.weather.models.CurrentWeather;
import com.ryanwelch.weather.ui.BaseFragment;
import com.ryanwelch.weather.ui.helpers.OnStartDragListener;
import com.ryanwelch.weather.ui.helpers.SimpleItemTouchHelperCallback;
import com.ryanwelch.weather.ui.helpers.VerticalSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends BaseFragment implements MainContract.View,
        OnStartDragListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainFragment";

    @Inject MainPresenter mMainPresenter;

    @BindView(R.id.weather_view) RecyclerView mRecyclerView;
    @BindView(R.id.weather_swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private WeatherListAdapter mWeatherListAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private MainListener mListener;

    public MainFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(MainComponent.class).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setupRecyclerView();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mMainPresenter.setView(this);
    }

    private void setupRecyclerView() {
        // Setup adapter
        mWeatherListAdapter = new WeatherListAdapter(getActivity(), new ArrayList<CurrentWeather>(), this);

        // Setup recycler view
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mWeatherListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics())));

        // Used for dragging and swipe to dismiss
        mItemTouchHelper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mWeatherListAdapter));
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showWeather(List<CurrentWeather> weatherList) {
        mWeatherListAdapter.setData(weatherList);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mMainPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mMainPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mMainPresenter.destroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainListener) {
            this.mListener = (MainListener) context;
        }
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

    @Override
    public void onRefresh() {
        mMainPresenter.onRefresh();
    }

    /**
     * Interface for listening to MainFragment events
     */
    public interface MainListener {

    }
}
