package com.ryanwelch.weather.data.search;

import android.content.Context;
import android.util.Log;

import com.ryanwelch.weather.BuildConfig;
import com.ryanwelch.weather.models.Place;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SearchProvider {

    private static final String TAG = "SearchProvider";

    private static final String SEARCH_BASE_URL = "http://api.apixu.com/v1/";
    private static final String SEARCH_API_KEY = BuildConfig.APIXU_API_TOKEN;

    private ArrayList<Place> mHistory = new ArrayList<>();
    private SearchService mSearchService;

    public SearchProvider(Context context) {
        Interceptor requestInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("key", SEARCH_API_KEY).build();
                request = request.newBuilder().url(url).build();

                return chain.proceed(request);
            }
        };

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(requestInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SEARCH_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mSearchService = retrofit.create(SearchService.class);
    }

    public ArrayList<Place> getHistory(int maxCount) {
        ArrayList<Place> suggestionList = new ArrayList<>();
        for (int i = 0; i < mHistory.size(); i++) {
            suggestionList.add(mHistory.get(i));
            if (suggestionList.size() == maxCount) {
                break;
            }
        }
        return suggestionList;
    }

    public void clearHistory() {
        mHistory.clear();
    }

    public void findSuggestions(String query, final OnFindListener listener) {

        Call<List<Place>> response = mSearchService.getSearch(query);

        response.enqueue(new Callback<List<Place>>() {
            @Override
            public void onResponse(Call<List<Place>> call, retrofit2.Response<List<Place>> response) {
                Log.v(TAG, "Received results");
                listener.onResults(response.body());
            }

            @Override
            public void onFailure(Call<List<Place>> call, Throwable t) {
                Log.v(TAG, "Failed to retrieve autocomplete: " + t.getMessage());
            }
        });
    }

    public interface OnFindListener {
        void onResults(List<Place> results);
    }

    public interface SearchService {

        @GET("search.json")
        Call<List<Place>> getSearch(@Query("q") String query);

    }
}
