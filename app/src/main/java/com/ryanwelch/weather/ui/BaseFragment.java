package com.ryanwelch.weather.ui;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.ryanwelch.weather.injector.HasComponent;

public class BaseFragment extends Fragment {

    /**
     * Shows a Toast message.
     */
    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Gets a component for dependency injection by its type.
     */
    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

}
