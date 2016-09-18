package com.ryanwelch.weather.ui.detailscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.injector.HasComponent;
import com.ryanwelch.weather.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity implements HasComponent<DetailComponent> {

    private static final String TAG = "DetailActivity";
    private static final String EXTRA_PLACE = "Place";

    @BindView(R.id.toolbar) Toolbar mToolbar;

    private DetailComponent mDetailComponent;

    public static Intent getCallingIntent(Context context, Place place) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_PLACE, place);
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

        Intent intent = this.getIntent();
        Place place = intent.getParcelableExtra(EXTRA_PLACE);

        initializeInjector();
        if (savedInstanceState == null) {
            addFragment(R.id.content_frame, DetailFragment.newInstance(place));
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
}
