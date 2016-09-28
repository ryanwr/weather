package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import rx.observers.TestSubscriber;

public class PlaceMemoryDataSourceTest {

    private static final Place TEST_PLACE_1 = new Place("London", "London", "United Kingdom", 0, 0);
    private static final Place TEST_PLACE_2 = new Place("New York", "MA", "United States", 0, 0);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addPlaceToStore() {
        PlaceMemoryDataSource placeMemoryDataSource = new PlaceMemoryDataSource();

        TestSubscriber<Void> addTestSubscriber = new TestSubscriber<>();
        placeMemoryDataSource.addPlace(TEST_PLACE_1).subscribe(addTestSubscriber);
        addTestSubscriber.assertNoErrors();
        addTestSubscriber.assertCompleted();

        TestSubscriber<List<Place>> getTestSubscriber = new TestSubscriber<>();
        placeMemoryDataSource.getPlaces().subscribe(getTestSubscriber);
        getTestSubscriber.assertNoErrors();
        getTestSubscriber.assertReceivedOnNext(Arrays.asList(Arrays.asList(TEST_PLACE_1)));
        getTestSubscriber.assertCompleted();
    }

}
