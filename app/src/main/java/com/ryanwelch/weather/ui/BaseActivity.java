package com.ryanwelch.weather.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.ryanwelch.weather.WeatherApplication;
import com.ryanwelch.weather.injector.components.ActivityComponent;
import com.ryanwelch.weather.injector.components.ApplicationComponent;
import com.ryanwelch.weather.injector.components.DaggerActivityComponent;
import com.ryanwelch.weather.injector.modules.ActivityModule;
import com.ryanwelch.weather.ui.navigation.Navigator;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    @Inject Navigator mNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getApplicationComponent().inject(this);

        initializeInjector();
    }

    private void initializeInjector() {
        mActivityComponent = DaggerActivityComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ApplicationComponent getApplicationComponent() {
        return WeatherApplication.from(this).getApplicationComponent();
    }

    public ActivityComponent getComponent() {
        return mActivityComponent;
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    protected Navigator getNavigator() {
        return mNavigator;
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
