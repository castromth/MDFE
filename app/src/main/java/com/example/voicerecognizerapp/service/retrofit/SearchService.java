package com.example.voicerecognizerapp.service.retrofit;

import com.example.voicerecognizerapp.model.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SearchService {




    @GET("search")
    Call<List<Item>> search(@Body String txt);


    @GET("search/{id}")
    Call<Item> getItem(@Path("id") Long id);
}
