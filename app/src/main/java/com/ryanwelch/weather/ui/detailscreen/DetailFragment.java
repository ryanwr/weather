/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.detailscreen;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.ui.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends BaseFragment implements DetailContract.View {

    private static final String TAG = "MainFragment";
    private static final String EXTRA_PLACE = "DetailPlace";
    private static final String EXTRA_TRANSITION_NAME = "DetailTransitionName";

    @Inject DetailPresenter mDetailPresenter;

    @BindView(R.id.view_pager) ViewPager mViewPager;
    @BindView(R.id.main_section) LinearLayout mMainLayout;

    private DetailListener mListener;
    private String mTransitionName;
    private Place mPlace;

    public static DetailFragment newInstance(Place place, String transitionName) {
        DetailFragment fragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PLACE, place);
        bundle.putString(EXTRA_TRANSITION_NAME, transitionName);
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
            mTransitionName = bundle.getString(EXTRA_TRANSITION_NAME);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMainLayout.setTransitionName(mTransitionName);
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
