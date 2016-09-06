package com.ryanwelch.weather.injector.components;

import android.content.Context;

import com.ryanwelch.weather.data.search.SearchProvider;
import com.ryanwelch.weather.injector.modules.ApplicationModule;
import com.ryanwelch.weather.injector.modules.NetModule;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;
import com.ryanwelch.weather.ui.BaseActivity;

import dagger.Component;
import retrofit2.Retrofit;

@ApplicationScope
@Component(modules = {ApplicationModule.class, NetModule.class})
public interface ApplicationComponent {

    void inject(BaseActivity activity);

    //Exposes Application Context to any component which depends on this
    Context getContext();
    SearchProvider getSearchProvider();
}
