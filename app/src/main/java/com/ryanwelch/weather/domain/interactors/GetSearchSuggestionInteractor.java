package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.search.SearchRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;

import rx.Observable;

public class GetSearchSuggestionInteractor extends Interactor {

    private final SearchRepository mSearchRepository;
    private final String mQuery;

    public GetSearchSuggestionInteractor(SearchRepository searchRepository,
                                         ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         String query) {
        super(threadExecutor, postExecutionThread);
        mSearchRepository = searchRepository;
        mQuery = query;
    }

    @Override
    protected Observable run() {
        return mSearchRepository.suggestionsList(mQuery);
    }

}
