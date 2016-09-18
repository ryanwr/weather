/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.ui.BaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class DetailFragment extends BaseFragment implements DetailContract.View {

    private static final String TAG = "MainFragment";
    private static final String EXTRA_PLACE = "Place";

    @Inject DetailPresenter mDetailPresenter;

    private DetailListener mListener;
    private Place mPlace;

    public static DetailFragment newInstance(Place place) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PLACE, place);
        fragment.setArguments(bundle);
        return fragment;
    }

    public DetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent(DetailComponent.class).inject(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mPlace = bundle.getParcelable(EXTRA_PLACE);
            mDetailPresenter.setPlace(mPlace);
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
