package com.ryanwelch.weather.ui.mainscreen;

import com.ryanwelch.weather.domain.interactors.DeletePlaceFactory;
import com.ryanwelch.weather.domain.interactors.DeletePlaceInteractor;
import com.ryanwelch.weather.domain.interactors.GetCurrentWeatherFactory;
import com.ryanwelch.weather.domain.interactors.GetCurrentWeatherInteractor;
import com.ryanwelch.weather.domain.models.Weather;
import com.ryanwelch.weather.domain.models.Place;
import com.ryanwelch.weather.domain.models.WeatherCondition;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import rx.Subscriber;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class MainPresenterTest {

    private static final Weather DEFAULT_WEATHER;

    static {
        DEFAULT_WEATHER = new Weather();
        DEFAULT_WEATHER.place = new Place("London", "London", "United Kingdom", 0, 0);
        DEFAULT_WEATHER.isDay = true;
        DEFAULT_WEATHER.weatherCondition = WeatherCondition.SUNNY;
    }

    @Mock GetCurrentWeatherFactory mGetCurrentWeatherFactory;
    @Mock GetCurrentWeatherInteractor mGetCurrentWeatherInteractor;
    @Mock DeletePlaceFactory mDeletePlaceFactory;
    @Mock DeletePlaceInteractor mDeletePlaceInteractor;
    @Mock MainContract.View mMainView;

    private MainPresenter mMainPresenter;

    @Captor
    private ArgumentCaptor<Subscriber> mSubscriber;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadCurrentWeatherData() {
        // When main presenter is resumed
        mMainPresenter = givenMainPresenter();
        when(mGetCurrentWeatherFactory.get()).thenReturn(mGetCurrentWeatherInteractor);
        mMainPresenter.resume();

        // Then shows a loading indicator, and subscribes to fetch weather
        verify(mMainView).showLoading();
        verify(mGetCurrentWeatherInteractor).execute(mSubscriber.capture());

        // When the weather is loaded
        ArrayList<Weather> data = new ArrayList();
        data.add(DEFAULT_WEATHER);
        mSubscriber.getValue().onNext(data);
        mSubscriber.getValue().onCompleted();

        verify(mMainView).showWeather(data);
        verify(mMainView).hideLoading();


    }

    @Test
    public void dismissCurrentWeatherItem() {
        // When an item is dismissed
        mMainPresenter = givenMainPresenter();
        when(mDeletePlaceFactory.get(any(Place.class))).thenReturn(mDeletePlaceInteractor);
        mMainPresenter.onItemDismiss(DEFAULT_WEATHER);

        // Get the interactor for that place, and execute it
        verify(mDeletePlaceFactory).get(DEFAULT_WEATHER.place);
        verify(mDeletePlaceInteractor).execute(any(Subscriber.class));
    }

    @Test
    public void selectCurrentWeatherItem() {
        // When an item is selected
        //mMainPresenter = givenMainPresenter();
        //mMainPresenter.onItemSelected(DEFAULT_WEATHER);

        // Launch detail view for place
        //verify(mMainView).showDetail(DEFAULT_WEATHER.place);
    }

    private MainPresenter givenMainPresenter() {
        MainPresenter mainPresenter = new MainPresenter();
        mainPresenter.mGetCurrentWeatherFactory = mGetCurrentWeatherFactory;
        mainPresenter.mDeletePlaceFactory = mDeletePlaceFactory;
        mainPresenter.setView(mMainView);
        return mainPresenter;
    }

}
