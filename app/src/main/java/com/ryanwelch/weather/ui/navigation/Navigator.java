package com.ryanwelch.weather.ui.navigation;

import android.content.Context;
import android.content.Intent;

import com.ryanwelch.weather.ui.searchscreen.SearchActivity;

public class Navigator {

    public Navigator() {

    }

    public void navigateToSearch(Context context) {
        if (context != null) {
            Intent intentToLaunch = SearchActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }
}
