package com.ryanwelch.weather.data;


public interface ResponseCallback<T> {

    void onSuccess(T data);

    void onFailure(String error);

}
