/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.settingsscreen;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.injector.components.ActivityComponent;
import com.ryanwelch.weather.ui.BaseActivity;
import com.ryanwelch.weather.ui.BaseFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View {

    @Inject SettingsPresenter mSettingsPresenter;

    private SettingsListener mListener;

    public SettingsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SettingsListener) {
            this.mListener = (SettingsListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    protected ActivityComponent getComponent() {
        return ((BaseActivity) getActivity()).getComponent();
    }

    /**
     * Interface for listening to SettingsFragment events
     */
    public interface SettingsListener {
    }

}
