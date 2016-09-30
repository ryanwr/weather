package com.ryanwelch.weather.ui.searchscreen;

import com.ryanwelch.weather.domain.interactors.AddPlaceFactory;
import com.ryanwelch.weather.domain.interactors.AddPlaceInteractor;
import com.ryanwelch.weather.domain.interactors.GetSearchSuggestionFactory;
import com.ryanwelch.weather.domain.interactors.GetSearchSuggestionInteractor;
import com.ryanwelch.weather.domain.models.Place;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import rx.Subscriber;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchPresenterTest {

    private static final Place TEST_PLACE_1 = new Place("London", "London", "United Kingdom", 0, 0);
    private static final Place TEST_PLACE_2 = new Place("New York", "MA", "United States", 10, 10);

    @Mock GetSearchSuggestionFactory mGetSearchSuggestionFactory;
    @Mock GetSearchSuggestionInteractor mGetSearchSuggestionInteractor;
    @Mock AddPlaceFactory mAddPlaceFactory;
    @Mock AddPlaceInteractor mAddPlaceInteractor;
    @Mock SearchContract.View mMainView;

    @Captor
    private ArgumentCaptor<Subscriber> mSubscriber;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadQuery() {
        SearchPresenter searchPresenter = givenSearchPresenter();
        when(mGetSearchSuggestionFactory.get(anyString())).thenReturn(mGetSearchSuggestionInteractor);

        // When load query
        searchPresenter.loadQuery("test");

        // Then shows a loading indicator, and subscribes to a search
        verify(mMainView).showLoading();
        verify(mGetSearchSuggestionInteractor).execute(mSubscriber.capture());

        // When the results are completed
        ArrayList<Place> data = new ArrayList();
        data.add(TEST_PLACE_1);
        data.add(TEST_PLACE_2);
        mSubscriber.getValue().onNext(data);
        mSubscriber.getValue().onCompleted();

        // Then show the data and hide loading indicator
        verify(mMainView).showSuggestions(data);
        verify(mMainView).hideLoading();
    }

    private SearchPresenter givenSearchPresenter() {
        SearchPresenter searchPresenter = new SearchPresenter();
        searchPresenter.mGetSearchSuggestionFactory = mGetSearchSuggestionFactory;
        searchPresenter.mAddPlaceFactory = mAddPlaceFactory;
        searchPresenter.setView(mMainView);
        return searchPresenter;
    }

}
