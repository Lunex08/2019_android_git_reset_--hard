package com.example.analyzer.utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONPlaceHolderApi {
    String URI_ALL_TARIFFS = "/tariffs/all.json";
    String URI_TARIFFS_OPERATORS = "/tariffs/operators.json";

    @GET(URI_TARIFFS_OPERATORS)
    Call<List<Operator>> getAllOperators();

    @GET(URI_ALL_TARIFFS)
    Call<List<Post>> getAllPosts();
}
