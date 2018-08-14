package com.rcacao.pocketmemes.data.api;

import com.rcacao.pocketmemes.BuildConfig;
import com.rcacao.pocketmemes.data.models.SearchResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleSearchService {

    @GET("v1?cx=" + BuildConfig.cx + "&key=" + BuildConfig.apikey + "&searchType=image")
    Call<SearchResult> getListImages(@Query("q") String search);

}
