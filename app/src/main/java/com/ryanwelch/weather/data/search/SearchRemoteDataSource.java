package com.ryanwelch.weather.data.search;

import com.ryanwelch.weather.domain.models.Place;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class SearchRemoteDataSource implements SearchDataSource {

    private static final String TAG = "SearchRemoteDataSource";

    private SearchService mSearchService;

    @Inject
    public SearchRemoteDataSource(Retrofit retrofit) {
        mSearchService = retrofit.create(SearchService.class);
    }

    public Observable<List<Place>> historyList() {
        return null;
    }

    public Observable<List<Place>> suggestionsList(String query) {
        return mSearchService.getSearch(query);
    }

    public interface SearchService {
        @GET("search.json")
        Observable<List<Place>> getSearch(@Query("q") String query);
    }
}
