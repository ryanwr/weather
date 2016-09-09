package com.ryanwelch.weather.domain.executor;

import com.ryanwelch.weather.injector.scopes.ApplicationScope;

import javax.inject.Inject;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

@ApplicationScope
public class UIThread implements PostExecutionThread {

    @Inject
    public UIThread() {}

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
