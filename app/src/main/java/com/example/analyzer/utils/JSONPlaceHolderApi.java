package com.example.analyzer.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONPlaceHolderApi {
    String URI = "/tariffs/example.json";

    @GET(URI)
    public Call<List<Post>> getAllPosts();
}
