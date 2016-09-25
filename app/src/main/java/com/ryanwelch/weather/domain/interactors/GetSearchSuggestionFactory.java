package com.ryanwelch.weather.domain.interactors;

import com.ryanwelch.weather.data.search.SearchRepository;
import com.ryanwelch.weather.domain.executor.PostExecutionThread;
import com.ryanwelch.weather.domain.executor.ThreadExecutor;

import javax.inject.Inject;

import dagger.Lazy;

public class GetSearchSuggestionFactory {

    private final Lazy<SearchRepository> mSearchRepository;
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;

    @Inject
    public GetSearchSuggestionFactory(Lazy<SearchRepository> searchRepository,
                                    ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread) {
        mSearchRepository = searchRepository;
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
    }

    public GetSearchSuggestionInteractor get(String query) {
        return new GetSearchSuggestionInteractor(mSearchRepository.get(), mThreadExecutor, mPostExecutionThread, query);
    }

}
