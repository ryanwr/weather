package com.ryanwelch.weather.ui.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.ui.detailscreen.DetailActivity;
import com.ryanwelch.weather.ui.mainscreen.WeatherListAdapter;
import com.ryanwelch.weather.ui.searchscreen.SearchActivity;
import com.ryanwelch.weather.ui.settingsscreen.SettingsActivity;

import javax.inject.Inject;

public class Navigator {

    public final static int REQUEST_SETTNGS = 100;

    @Inject
    public Navigator() {

    }

    public void navigateToSearch(Context context) {
        if (context != null) {
            Intent intentToLaunch = SearchActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    public void navigateToSettings(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = SettingsActivity.getCallingIntent(activity);
            activity.startActivityForResult(intentToLaunch, REQUEST_SETTNGS);
        }
    }

    public void navigateToDetail(Activity activity, Weather weather, WeatherListAdapter.WeatherItemViewHolder viewHolder) {
        if (activity != null) {
            Intent intentToLaunch = DetailActivity.getCallingIntent(activity, weather, viewHolder.getTransitionId());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Pair<View, String> p1 = new Pair<>(viewHolder.itemView, viewHolder.itemView.getTransitionName());
                Pair<View, String> p2 = new Pair<>(viewHolder.mWeatherIcon, viewHolder.mWeatherIcon.getTransitionName());
                Pair<View, String> p3 = new Pair<>(viewHolder.mLocationName, viewHolder.mLocationName.getTransitionName());
                Pair<View, String> p4 = new Pair<>(viewHolder.mTemperature, viewHolder.mTemperature.getTransitionName());
                Pair<View, String> p5 = new Pair<>(viewHolder.mCondition, viewHolder.mCondition.getTransitionName());
                ActivityOptionsCompat options = ActivityOptionsCompat
                        .makeSceneTransitionAnimation(activity, p1, p2, p3, p4, p5);
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
