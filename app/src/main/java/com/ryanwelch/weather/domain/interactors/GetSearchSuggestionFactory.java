package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.search.SearchRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;

import javax.inject.Inject;

public class GetSearchSuggestionFactory {

    private final SearchRepository mSearchRepository;
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;

    @Inject
    public GetSearchSuggestionFactory(SearchRepository searchRepository,
                                    ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        mSearchRepository = searchRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    public GetSearchSuggestionInteractor get(String query) {
        return new GetSearchSuggestionInteractor(mSearchRepository, mThreadExecutor, mPostExecutionThread, query);
    }

}
