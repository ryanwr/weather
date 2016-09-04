package com.ryanwelch.weather.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.models.Place;
import com.ryanwelch.weather.ui.fragments.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements MainFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    private static final int SEARCH_REQUEST_CODE = 1001;

    //@BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.btn_add) Button mButtonAdd;

    //private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        ButterKnife.bind(this);

        // Use support library action bar instead of default
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        //mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");

        if (savedInstanceState == null) {
            MainFragment fragment = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment, "MainFragment")
                    .commit();
        }
    }

    public void onPlaceSelected(Place place) {
        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("MainFragment");
        mainFragment.addWeatherLocation(place);
    }

    @OnClick(R.id.btn_add)
    public void onButtonAddClick() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivityForResult(intent, SEARCH_REQUEST_CODE);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_REQUEST_CODE) {
            if(resultCode == RESULT_OK){
                Place place = data.getParcelableExtra("result");
                onPlaceSelected(place);
            }
            if (resultCode == RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
