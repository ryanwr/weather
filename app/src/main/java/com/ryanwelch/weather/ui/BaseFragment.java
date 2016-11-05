package com.ryanwelch.weather.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.ryanwelch.weather.WeatherApplication;
import com.ryanwelch.weather.injector.components.ActivityComponent;
import com.ryanwelch.weather.injector.components.ApplicationComponent;
import com.squareup.leakcanary.RefWatcher;

import javax.inject.Inject;

public class BaseFragment extends Fragment {

    @Inject RefWatcher mRefWatcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        // We're about to be GC'd, make sure we get GC'd
        mRefWatcher.watch(this);
        super.onDestroy();
    }

    protected ApplicationComponent getApplicationComponent() {
        return WeatherApplication.from(getActivity()).getApplicationComponent();
    }

    /**
     * Shows a Toast message.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets a component for dependency injection.
     */
    protected ActivityComponent getComponent() {
        return ((BaseActivity) getActivity()).getComponent();
    }

}
