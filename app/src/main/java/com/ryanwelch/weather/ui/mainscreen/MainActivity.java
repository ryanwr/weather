package com.ryanwelch.weather.ui.mainscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.WeatherApplication;
import com.ryanwelch.weather.domain.models.CurrentWeather;
import com.ryanwelch.weather.injector.components.ActivityComponent;
import com.ryanwelch.weather.injector.components.DaggerActivityComponent;
import com.ryanwelch.weather.ui.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainFragment.MainListener {

    private final static int CODE_SETTINGS = 400;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Places");
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_add_white);

        if (savedInstanceState == null) {
            addFragment(R.id.content_frame, new MainFragment());
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO: If data source setting changed, recreate app component and recreate Activity.
        //((WeatherApplication) mApplication).buildApplicationComponent();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                getNavigator().navigateToSettings(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
