package com.example.ryan.weather.ui.activities;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.example.ryan.weather.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.txt_temperature) TextView mTextTemperature;

    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind views
        ButterKnife.bind(this);

        // Use support library action bar instead of default
        setSupportActionBar(mToolbar);

        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("London");

        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/Oswald-Regular.ttf");

        mTextTemperature.setTypeface(mTypeface);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
