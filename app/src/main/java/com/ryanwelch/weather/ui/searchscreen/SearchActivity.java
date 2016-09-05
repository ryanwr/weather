package com.ryanwelch.weather.ui.searchscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.ui.BaseActivity;

public class SearchActivity extends BaseActivity implements SearchFragment.SearchListener {

    private static final String TAG = "SearchActivity";

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SearchActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (savedInstanceState == null) {
            addFragment(R.id.content_frame, new SearchFragment());
        }
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
