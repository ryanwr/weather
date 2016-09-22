/*
 * Copyright (c) 2016 Ryan Welch. All rights reserved.
 */

package com.ryanwelch.weather.injector.modules;

import com.ryanwelch.weather.injector.scopes.ApplicationScope;
import com.squareup.leakcanary.RefWatcher;

import dagger.Module;
import dagger.Provides;

@Module
public class DebugModule {

    private final RefWatcher mRefWatcher;

    public DebugModule(RefWatcher refWatcher) {
        mRefWatcher = refWatcher;
    }

    @Provides
    @ApplicationScope
    RefWatcher providesRefWatcher() {
        return mRefWatcher;
    }

}
