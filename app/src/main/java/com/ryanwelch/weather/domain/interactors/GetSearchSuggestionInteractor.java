package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.search.SearchRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;

import javax.inject.Inject;

import rx.Subscriber;

public class GetSearchSuggestionInteractor extends Interactor {

    private final SearchRepository mSearchRepository;

    @Inject
    public GetSearchSuggestionInteractor(SearchRepository searchRepository, ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        mSearchRepository = searchRepository;
    }

    public void execute(String query, Subscriber subscriber) {
        super.execute(mSearchRepository.suggestionsList(query), subscriber);
    }
}
