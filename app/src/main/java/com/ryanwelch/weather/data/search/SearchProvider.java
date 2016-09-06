package com.ryanwelch.weather.data.search;

import com.ryanwelch.weather.models.Place;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class SearchProvider {

    private static final String TAG = "SearchProvider";

    private ArrayList<Place> mHistory = new ArrayList<>();
    private SearchService mSearchService;

    @Inject
    public SearchProvider(Retrofit retrofit) {
        mSearchService = retrofit.create(SearchService.class);
    }

    public Observable<List<Place>> getHistory(int maxCount) {
        ArrayList<Place> suggestionList = new ArrayList<>();
        for (int i = 0; i < mHistory.size(); i++) {
            suggestionList.add(mHistory.get(i));
            if (suggestionList.size() == maxCount) {
                break;
            }
        }
        return Observable.just(suggestionList);
    }

    public void clearHistory() {
        mHistory.clear();
    }

    public Observable<List<Place>> findSuggestions(String query) {
        return mSearchService.getSearch(query);
    }

    public interface SearchService {
        @GET("search.json")
        Observable<List<Place>> getSearch(@Query("q") String query);
    }
}
