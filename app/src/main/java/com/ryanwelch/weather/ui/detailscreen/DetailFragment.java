/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.ui.BaseFragment;
import com.ryanwelch.weather.ui.components.WeatherIconView;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends BaseFragment implements DetailContract.View {

    private static final String TAG = "MainFragment";
    private static final String EXTRA_DATA = "DetailData";
    private static final String EXTRA_TRANSITION_NAME = "DetailTransitionName";

    @Inject DetailPresenter mDetailPresenter;

    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.main_section) LinearLayout mMainLayout;
    @BindView(R.id.weather_icon) WeatherIconView mWeatherIcon;
    @BindView(R.id.txt_location) TextView mLocation;
    @BindView(R.id.txt_condition) TextView mCondition;
    @BindView(R.id.txt_temperature) TextView mTemperature;
    @BindView(R.id.txt_feels_like) TextView mFeelsLike;

    @BindView(R.id.txt_wind_dir) TextView mWindDir;

    @BindString(R.string.temperature_format) String mTemperatureFormat;
    @BindString(R.string.feels_like_format) String mFeelsLikeFormat;

    private DetailListener mListener;
    private String mTransitionId;

    public static DetailFragment newInstance(CurrentWeather weather, String transitionId) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DATA, weather);
        bundle.putString(EXTRA_TRANSITION_NAME, transitionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    public DetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(DetailComponent.class).inject(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mDetailPresenter.setData(bundle.getParcelable(EXTRA_DATA));
            mTransitionId = bundle.getString(EXTRA_TRANSITION_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mDetailPresenter.setView(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMainLayout.setTransitionName("weather_item_" + mTransitionId);
            mWeatherIcon.setTransitionName("weather_icon_" + mTransitionId);
            mMainLayout.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mMainLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getActivity().startPostponedEnterTransition();
                    }
                    return true;
                }
            });
        }

        setupViewPager();
    }

    private void setupViewPager() {
        mViewPager.setAdapter(new DetailPagerAdapter(getChildFragmentManager()));
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mDetailPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.mDetailPresenter.pause();
    }

    @Override
    public void onDestroy() {
        this.mDetailPresenter.destroy();
        super.onDestroy();
    }

    @Override
    public void showData(CurrentWeather data) {
        mMainLayout.setBackgroundColor(
                ContextCompat.getColor(
                        getContext(),
                        data.weatherCondition.getIcon().getColor()));
        mWeatherIcon.setIcon(data.isDay ? data.weatherCondition.getIcon() : data.weatherCondition.getNightIcon());
        mLocation.setText(data.place.getName());
        mCondition.setText(data.isDay ? data.weatherCondition.getName() : data.weatherCondition.getNightName());
        mFeelsLike.setText(String.format(mFeelsLikeFormat, (long) Math.round(data.feelsLikeC)));
        mTemperature.setText(String.format(mTemperatureFormat, (long) Math.round(data.temperatureC)));

        mWindDir.setText(data.windDirection);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /**
     * Interface for listening to DetailFragment events
     */
    public interface DetailListener {

    }
}
