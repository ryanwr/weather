package com.ryanwelch.weather.ui.mainscreen;

import android.os.Bundle;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.injector.HasComponent;
import com.ryanwelch.weather.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements HasComponent<MainComponent>,
        MainFragment.MainListener {

    private MainComponent mMainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        ButterKnife.bind(this);

        initializeInjector();
        if (savedInstanceState == null) {
            addFragment(R.id.content_frame, new MainFragment());
        }
    }

    private void initializeInjector() {
        mMainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @OnClick(R.id.btn_add)
    public void onButtonAddClick() {
        getNavigator().navigateToSearch(this);
    }

    @Override
    public void showDetail(CurrentWeather weather, WeatherListAdapter.WeatherItemViewHolder viewHolder) {
        getNavigator().navigateToDetail(this, weather, viewHolder);
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }

}
