package com.ryanwelch.weather.injector.components;

import android.content.Context;

import com.ryanwelch.weather.injector.modules.ApplicationModule;
import com.ryanwelch.weather.injector.modules.NetModule;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;
import com.ryanwelch.weather.ui.BaseActivity;

import dagger.Component;

@ApplicationScope
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseActivity activity);

    NetComponent plus(NetModule netModule);

    //Exposes Application Context to any component which depends on this
    Context getContext();
}
