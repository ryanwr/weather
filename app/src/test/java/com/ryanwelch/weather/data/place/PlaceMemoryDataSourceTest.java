package com.ryanwelch.weather.data.place;

import com.ryanwelch.weather.domain.models.Place;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import rx.observers.TestSubscriber;

public class PlaceMemoryDataSourceTest {

    private static final Place TEST_PLACE_1 = new Place("London", "London", "United Kingdom", 0, 0);
    private static final Place TEST_PLACE_2 = new Place("New York", "MA", "United States", 10, 10);

    @Test
    public void addPlaceToStore() {
        PlaceMemoryDataSource placeMemoryDataSource = new PlaceMemoryDataSource();

        // When a place is added
        TestSubscriber<Void> addTestSubscriber = new TestSubscriber<>();
        placeMemoryDataSource.addPlace(TEST_PLACE_1).subscribe(addTestSubscriber);

        // Make sure it adds with no errors
        addTestSubscriber.assertNoErrors();

        // And when retrieved has that place
        TestSubscriber<List<Place>> getTestSubscriber = new TestSubscriber<>();
        placeMemoryDataSource.getPlaces().subscribe(getTestSubscriber);
        getTestSubscriber.assertNoErrors();
        getTestSubscriber.assertReceivedOnNext(Arrays.asList(Arrays.asList(TEST_PLACE_1)));
    }

    @Test
    public void addDuplicatePlaceToStore() {
        PlaceMemoryDataSource placeMemoryDataSource = new PlaceMemoryDataSource();

        // When a place is added more than once
        placeMemoryDataSource.addPlace(TEST_PLACE_1).subscribe();
        placeMemoryDataSource.addPlace(TEST_PLACE_1).subscribe();

        // Make sure it only saves that place once and does not store a duplicate
        TestSubscriber<List<Place>> getTestSubscriber = new TestSubscriber<>();
        placeMemoryDataSource.getPlaces().subscribe(getTestSubscriber);
        getTestSubscriber.assertNoErrors();
        getTestSubscriber.assertReceivedOnNext(Arrays.asList(Arrays.asList(TEST_PLACE_1)));
    }

    @Test
    public void removePlaceFromStore() {
        PlaceMemoryDataSource placeMemoryDataSource = new PlaceMemoryDataSource();
        placeMemoryDataSource.setPlaces(Arrays.asList(TEST_PLACE_1, TEST_PLACE_2)).subscribe();

        // When a place is removed
        TestSubscriber<Void> removeTestSubscriber = new TestSubscriber<>();
        placeMemoryDataSource.removePlace(TEST_PLACE_2).subscribe(removeTestSubscriber);

        // Make sure it removes with no errors
        removeTestSubscriber.assertNoErrors();

        // And make sure it was removed
        TestSubscriber<List<Place>> getTestSubscriber = new TestSubscriber<>();
        placeMemoryDataSource.getPlaces().subscribe(getTestSubscriber);
        getTestSubscriber.assertNoErrors();
        getTestSubscriber.assertReceivedOnNext(Arrays.asList(Arrays.asList(TEST_PLACE_1)));
    }

}
