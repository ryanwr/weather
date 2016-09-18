package com.ryanwelch.weather.ui.navigation;

import android.content.Context;
import android.content.Intent;

import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.ui.detailscreen.DetailActivity;
import com.ryanwelch.weather.ui.searchscreen.SearchActivity;

import javax.inject.Inject;

public class Navigator {

    @Inject
    public Navigator() {

    }

    public void navigateToSearch(Context context) {
        if (context != null) {
            Intent intentToLaunch = SearchActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToDetail(Context context, Place place) {
        if (context != null) {
            Intent intentToLaunch = DetailActivity.getCallingIntent(context, place);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateBack(Context context) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
