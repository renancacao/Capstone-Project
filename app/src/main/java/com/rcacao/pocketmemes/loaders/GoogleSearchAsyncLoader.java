package com.rcacao.pocketmemes.loaders;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.rcacao.pocketmemes.data.api.ApiRetrofit;
import com.rcacao.pocketmemes.data.api.GoogleSearchService;
import com.rcacao.pocketmemes.data.models.SearchResult;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoogleSearchAsyncLoader extends AsyncTaskLoader<SearchResult> {

    public static final String ARG_SEARCH = "search";
    private Bundle args;
    private SearchResult result;

    public GoogleSearchAsyncLoader(@NonNull Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    @Override
    protected void onStartLoading() {
       if(result==null){
           forceLoad();
       }
       else{
           deliverResult(result);
       }
    }

    @Nullable
    @Override
    public SearchResult loadInBackground() {

        String search = "";
        if (args.containsKey(ARG_SEARCH)){
            search = args.getString(ARG_SEARCH);
        }

        if (search == null || search.isEmpty()) {
            return null;
        }


        Retrofit retrofit = ApiRetrofit.getRetrofit();
        GoogleSearchService service = retrofit.create(GoogleSearchService.class);

        Call<SearchResult> call = service.getListImages(search);

        try {
            Response<SearchResult> response = call.execute();
            if (response != null){
                if (response.body() != null) {
                    result = response.body();
                }
            }

        } catch (IOException e) {
            result = null;

        }

        return result;

    }


}
