package com.ryanwelch.weather.ui.mainscreen;

import android.os.Bundle;

import com.ryanwelch.weather.R;
import com.ryanwelch.weather.ui.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            addFragment(R.id.content_frame, new MainFragment());
        }
    }

    @OnClick(R.id.btn_add)
    public void onButtonAddClick() {
        getNavigator().navigateToSearch(this);
    }
}
