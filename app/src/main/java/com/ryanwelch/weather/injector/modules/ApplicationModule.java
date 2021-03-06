package com.ryanwelch.weather.injector.modules;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.ryanwelch.weather.data.place.PlaceLocalDataSource;
import com.ryanwelch.weather.data.place.PlaceMemoryDataSource;
import com.ryanwelch.weather.data.place.PlaceRepository;
import com.ryanwelch.weather.data.place.PlaceSharedPreferencesDataSource;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;
import com.ryanwelch.weather.domain.executor.UIThread;
import com.ryanwelch.weather.injector.scopes.ApplicationScope;
import com.ryanwelch.weather.ui.navigation.Navigator;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationScope
    Context providesApplicationContext() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationScope
    Navigator providesNavigator() {
        return new Navigator();
    }

    @Provides
    @ApplicationScope
    ReactiveLocationProvider providesLocationProvider(Context context) {
        return new ReactiveLocationProvider(context);
    }

    @Provides
    @ApplicationScope
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @ApplicationScope
    ThreadExecutor provideThreadExecutor() {
        return new ThreadExecutor();
    }

    @Provides
    @ApplicationScope
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides
    @ApplicationScope
    PlaceRepository providePlaceRepository(StorIOSQLite storIOSQLite) {
        return new PlaceRepository(new PlaceMemoryDataSource(), new PlaceLocalDataSource(storIOSQLite));
    }
}
