package com.ryanwelch.weather.data.helper;


public interface ResponseCallback<T> {

    void onSuccess(T data);

    void onFailure(String error);

}
