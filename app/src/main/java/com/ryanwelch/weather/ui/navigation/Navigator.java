package com.ryanwelch.weather.ui.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

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

    public void navigateToDetail(Activity activity, Place place, View transitionView) {
        if (activity != null) {
            Intent intentToLaunch = DetailActivity.getCallingIntent(activity, place, transitionView.getTransitionName());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, transitionView, transitionView.getTransitionName());
                activity.startActivity(intentToLaunch, options.toBundle());
            } else {
                activity.startActivity(intentToLaunch);
            }
        }
    }

    public void navigateBack(Context context) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
