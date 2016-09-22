package com.ryanwelch.weather.ui.detailscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.injector.HasComponent;
import com.ryanwelch.weather.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements HasComponent<DetailComponent> {

    private static final String TAG = "DetailActivity";
    private static final String EXTRA_DATA = "DetailData";
    private static final String EXTRA_TRANSITION_NAME = "DetailTransitionName";

    @BindView(R.id.toolbar) Toolbar mToolbar;

    private DetailComponent mDetailComponent;

    public static Intent getCallingIntent(Context context, CurrentWeather weather, String transitionName) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_DATA, weather);
        intent.putExtra(EXTRA_TRANSITION_NAME, transitionName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Bind views
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition();
        }

        Intent intent = this.getIntent();
        CurrentWeather weather = intent.getParcelableExtra(EXTRA_DATA);
        String transitionName = intent.getStringExtra(EXTRA_TRANSITION_NAME);

        initializeInjector();
        if (savedInstanceState == null) {
            addFragment(R.id.content_frame, DetailFragment.newInstance(weather, transitionName));
        }
    }

    private void initializeInjector() {
        mDetailComponent = DaggerDetailComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public DetailComponent getComponent() {
        return mDetailComponent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Build.VERSION.SDK_INT > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
