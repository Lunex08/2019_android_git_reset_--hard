package com.example.analyzer.utils;

import com.example.analyzer.fragments.TariffsFragment;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;
//    /tariffs/example.json
    private static final String BASE_URL = "https://static.brbrroman.ru";
    private Retrofit mRetrofit;

    private NetworkService() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static NetworkService getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkService();
        }
        return mInstance;
    }

    public TariffsFragment.JSONPlaceHolderApi getJSONApi() {
        return mRetrofit.create(TariffsFragment.JSONPlaceHolderApi.class);
    }
}


