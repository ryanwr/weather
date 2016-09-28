package com.ryanwelch.weather.data.place;


import android.content.SharedPreferences;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ryanwelch.weather.domain.models.Place;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class PlaceSharedPreferencesDataSourceTest {

    private static final Place TEST_PLACE_1 = new Place("London", "London", "United Kingdom", 0, 0);
    private static final Place TEST_PLACE_2 = new Place("New York", "MA", "United States", 10, 10);

    private static final String SERIALIZED_PLACES = "[{\"m_id\":0.0,\"lat\":0.0,\"lon\":0.0," +
            "\"name\":\"London\",\"region\":\"London\",\"country\":\"United Kingdom\"}," +
            "{\"m_id\":0.0,\"lat\":10.0,\"lon\":10.0,\"name\":\"New York\",\"region\":\"MA\"," +
            "\"country\":\"United States\"}]";

    private PlaceSharedPreferencesDataSource mPlaceSharedPreferencesDataSource;
    private Gson mGson;

    @Mock SharedPreferences.Editor mEditor;
    @Mock SharedPreferences mSharedPreferences;

    @Before
    public void setup() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        mGson = gsonBuilder.create();

        MockitoAnnotations.initMocks(this);

        mPlaceSharedPreferencesDataSource = new PlaceSharedPreferencesDataSource(mSharedPreferences, mGson);
    }

    @Test
    public void setPlacesInStore() {
        when(mSharedPreferences.edit()).thenReturn(mEditor);
        when(mEditor.putString(anyString(), anyString())).thenReturn(mEditor);

        // When set
        TestSubscriber<Void> testSubscriber = new TestSubscriber<>();
        mPlaceSharedPreferencesDataSource.setPlaces(Arrays.asList(TEST_PLACE_1, TEST_PLACE_2))
                .subscribe(testSubscriber);
        testSubscriber.assertNoErrors();

        // Verify put into shared preferences
        verify(mSharedPreferences).edit();
        verify(mEditor).putString(anyString(), eq(SERIALIZED_PLACES));
        verify(mEditor).apply();
    }

    @Test
    public void getPlacesFromStore() {
        when(mSharedPreferences.getString(anyString(), anyString()))
                .thenReturn(SERIALIZED_PLACES);

        // When get
        TestSubscriber<List<Place>> testSubscriber = new TestSubscriber<>();
        mPlaceSharedPreferencesDataSource.getPlaces().subscribe(testSubscriber);
        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(Arrays.asList(TEST_PLACE_1, TEST_PLACE_2));

        // Verify get from shared preferences
        verify(mSharedPreferences).getString(anyString(), anyString());
    }

}
