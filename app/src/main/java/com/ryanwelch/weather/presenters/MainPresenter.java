package com.ryanwelch.weather.presenters;

public class MainPresenter implements MainContract.Presenter {

    private MainContract.View mView;

    @Override
    public void setView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }
}
