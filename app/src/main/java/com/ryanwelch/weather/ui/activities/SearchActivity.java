package com.ryanwelch.weather.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.models.Place;
import com.ryanwelch.weather.ui.fragments.SearchFragment;

public class SearchActivity extends BaseActivity {

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            SearchFragment fragment = SearchFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment, "SearchFragment")
                    .commit();
        }
    }

    public void returnResult(Place place) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", place);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    public void returnCancel() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

}
