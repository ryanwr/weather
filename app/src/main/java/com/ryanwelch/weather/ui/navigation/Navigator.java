package com.ryanwelch.weather.ui.navigation;

import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Navigator {

    @Inject
    public Navigator() {

    }

    public void navigateToDetails(Context context) {
        if (context != null) {
            //Intent intentToLaunch = UserListActivity.getCallingIntent(context);
            //context.startActivity(intentToLaunch);
        }
    }
}
