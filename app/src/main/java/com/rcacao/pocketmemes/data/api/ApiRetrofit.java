package com.rcacao.pocketmemes.data.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiRetrofit {

    private static final String BASE_URL = "https://www.googleapis.com/customsearch/";

    private static Retrofit retrofit = null;

    private ApiRetrofit() {}

    public static Retrofit getRetrofit(){

        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }

        return retrofit;
    }

}
