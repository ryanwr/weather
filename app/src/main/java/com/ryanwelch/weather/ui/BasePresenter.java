package com.ryanwelch.weather.ui;

/**
 * Interface representing a BasePresenter in MVP
 */
public interface BasePresenter<T> {

    void setView(T view);

    void resume();

    void pause();

    void destroy();

}
