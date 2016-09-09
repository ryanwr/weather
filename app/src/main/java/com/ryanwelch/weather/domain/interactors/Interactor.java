package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

public abstract class Interactor {

    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;

    private Subscription subscription = Subscriptions.empty();

    protected Interactor(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    @SuppressWarnings("unchecked")
    protected void execute(Observable observable, Subscriber subscriber) {
        unsubscribe();
        this.subscription = observable
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(subscriber);
    }

    public void unsubscribe() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
