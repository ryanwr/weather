package com.ryanwelch.weather.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.ryanwelch.weather.R;
import com.ryanwelch.weather.ui.fragments.MainFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    private static final int REQUEST_SELECT_PLACE = 1000;

    //@BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.btn_add) Button mButtonAdd;
    @BindView(R.id.floating_search_view) FloatingSearchView mFloatingSearchView;

    private RecyclerView mSearchResultsList;
    //private SearchResultsListAdapter mSearchResultsAdapter;
    private String mLastQuery = "";
    private Typeface mTypeface;

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

        //mTextTemperature.setTypeface(EasyFonts.robot(this));

//        mSearchResultsAdapter = new SearchResultsListAdapter();
//        mSearchResultsList.setAdapter(mSearchResultsAdapter);
//        mSearchResultsList.setLayoutManager(new LinearLayoutManager(getContext()));
//
//        mFloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
//
//            @Override
//            public void onSearchTextChanged(String oldQuery, final String newQuery) {
//
//                if (!oldQuery.equals("") && newQuery.equals("")) {
//                    mFloatingSearchView.clearSuggestions();
//                } else {
//                    mFloatingSearchView.showProgress();
//
//                    DataHelper.findSuggestions(getActivity(), newQuery, 5,
//                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {
//
//                                @Override
//                                public void onResults(List<ColorSuggestion> results) {
//
//                                    //this will swap the data and
//                                    //render the collapse/expand animations as necessary
//                                    mFloatingSearchView.swapSuggestions(results);
//
//                                    mFloatingSearchView.hideProgress();
//                                }
//                            });
//                }
//            }
//        });
//
//        mFloatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
//            @Override
//            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
//
//                ColorSuggestion colorSuggestion = (ColorSuggestion) searchSuggestion;
//                DataHelper.findColors(getActivity(), colorSuggestion.getBody(),
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                mSearchResultsAdapter.swapData(results);
//                            }
//
//                        });
//                Log.d(TAG, "onSuggestionClicked()");
//
//                mLastQuery = searchSuggestion.getBody();
//            }
//
//            @Override
//            public void onSearchAction(String query) {
//                mLastQuery = query;
//
//                DataHelper.findColors(getActivity(), query,
//                        new DataHelper.OnFindColorsListener() {
//
//                            @Override
//                            public void onResults(List<ColorWrapper> results) {
//                                mSearchResultsAdapter.swapData(results);
//                            }
//
//                        });
//                Log.d(TAG, "onSearchAction()");
//            }
//        });


        mFloatingSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                //show suggestions when search bar gains focus (typically history suggestions)
                //mSearchView.swapSuggestions(DataHelper.getHistory(getActivity(), 3));

                // TODO: Show current location as suggestion
            }

            @Override
            public void onFocusCleared() {
                mFloatingSearchView.animate()
                        .translationY(-mFloatingSearchView.getHeight())
                        .alpha(0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                mFloatingSearchView.setVisibility(View.GONE);
                            }
                        });
            }
        });

//        mFloatingSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
//            @Override
//            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
//                                         TextView textView, SearchSuggestion item, int itemPosition) {
//                ColorSuggestion colorSuggestion = (ColorSuggestion) item;
//
//                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
//                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";
//
//                if (colorSuggestion.getIsHistory()) {
//                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.ic_history_black_24dp, null));
//
//                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
//                    leftIcon.setAlpha(.36f);
//                } else {
//                    leftIcon.setAlpha(0.0f);
//                    leftIcon.setImageDrawable(null);
//                }
//
//                textView.setTextColor(Color.parseColor(textColor));
//                String text = colorSuggestion.getBody()
//                        .replaceFirst(mSearchView.getQuery(),
//                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
//                textView.setText(Html.fromHtml(text));
//            }
//
//        });

        mFloatingSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });

        if (savedInstanceState == null) {
            MainFragment fragment = MainFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_frame, fragment)
                    .commit();
        }
    }

    @OnClick(R.id.btn_add)
    public void onButtonAddClick() {
//        mFloatingSearchView.setVisibility(View.VISIBLE);
//        mFloatingSearchView.setAlpha(0f);
//        mFloatingSearchView.setTranslationY(-mFloatingSearchView.getHeight());
//        mFloatingSearchView.animate()
//                .translationY(0)
//                .alpha(1f)
//                .setDuration(300)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        mFloatingSearchView.setSearchFocused(true);
//                    }
//                }
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder
                    (PlaceAutocomplete.MODE_OVERLAY)
                    .build(MainActivity.this);
            startActivityForResult(intent, REQUEST_SELECT_PLACE);
        } catch (GooglePlayServicesRepairableException |
                GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
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
        if (requestCode == REQUEST_SELECT_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.v("Test", place.getName().toString());
                //this.onPlaceSelected(place);
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                //this.onError(status);
                Log.v("Test", status.getStatusMessage());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
