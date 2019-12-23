package com.example.analyzer.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONPlaceHolderApi {
    String URI = "/tariffs/all.json";
    String URI2 = "/tariffs/operators.json";

    @GET(URI2)
    public Call<List<Operator>> getAllOperators();

    @GET(URI)
    public Call<List<Post>> getAllPosts();
}
