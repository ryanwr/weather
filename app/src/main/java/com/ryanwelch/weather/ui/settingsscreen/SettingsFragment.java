/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.ui.settingsscreen;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.View;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.injector.components.ActivityComponent;
import com.ryanwelch.weather.ui.BaseActivity;

import javax.inject.Inject;

import timber.log.Timber;

public class SettingsFragment extends PreferenceFragmentCompat implements SettingsContract.View {

    @Inject SettingsPresenter mSettingsPresenter;

    private Preference mVersionPreference;
    private Preference mFlavorPreference;
    private ListPreference mWeatherProviderPreference;
    private ListPreference mUnitPreference;
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
        bindPreferences();
    }

    private void bindPreferences() {
        mVersionPreference = findPreference(getString(R.string.version_preference_key));
        mFlavorPreference = findPreference(getString(R.string.flavor_preference_key));
        mWeatherProviderPreference = (ListPreference) findPreference(getString(R.string.weather_provider_preference_key));
        mUnitPreference = (ListPreference) findPreference(getString(R.string.unit_preference_key));
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        mSettingsPresenter.setView(this);
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

    @Override
    public void setVersionSummary(String version) {
        mVersionPreference.setSummary(version);
    }

    @Override
    public void setFlavorSummary(String flavor) {
        mFlavorPreference.setSummary(flavor);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSettingsPresenter.resume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(mSettingsPresenter);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(mSettingsPresenter);
        mSettingsPresenter.pause();
        super.onPause();
    }

    /**
     * Interface for listening to SettingsFragment events
     */
    public interface SettingsListener {
    }

}
