package com.example.voicerecognizerapp.service.retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private String url = "";
    private Retrofit retrofit;

    public void RetrofitService(){
        retrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }

    public SearchService getSearchService(){
        return retrofit.create(SearchService.class);
    }
}
