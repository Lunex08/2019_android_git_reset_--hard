package com.example.analyzer.service.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.analyzer.service.model.JsonService;
import com.example.analyzer.service.model.Operator;
import com.example.analyzer.service.model.Post;
import com.example.analyzer.service.model.TariffDataset;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TariffRepository {
    private static final TariffRepository mInstance = new TariffRepository();
    private final String BASE_URL = "https://static.brbrroman.ru";
    private Retrofit mRetrofit;
    private MutableLiveData<List<TariffDataset>> tariffsObservable = new MutableLiveData<>();
    private MutableLiveData<List<Operator>> operatorsObservable = new MutableLiveData<>();
    private JsonService mJsonAPI;

    private TariffRepository() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mJsonAPI = mRetrofit.create(JsonService.class);
        tariffsObservable.setValue(new ArrayList<>());
        operatorsObservable.setValue(new ArrayList<>());
    }

    public static TariffRepository getInstance() {
        return mInstance;
    }

    public LiveData<List<TariffDataset>> getTariffs() {
        return tariffsObservable;
    }

    public void refreshTariffs() {
        mJsonAPI.getAllPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                List<Post> posts = response.body();

                if (posts != null) {
                    List<TariffDataset> tariffs = new ArrayList<>(); // проверить как работает тут
                    for (Post post : posts) {
                        Double price =
                                BigDecimal.valueOf(post.getPrice()).setScale(0, RoundingMode.HALF_UP).doubleValue();
                        assert tariffs != null;
                        tariffs.add(new TariffDataset(post.getName(), post.getTraffic(), post.getSms(),
                                String.valueOf(price), post.getOperator(), post.getId()));
                    }
                    Collections.sort(tariffs, (o1, o2) -> {
                        double first = Double.valueOf(o1.getPrice());
                        double second = Double.valueOf(o2.getPrice());
                        if (first > second)
                            return 1;
                        if (first < second)
                            return -1;
                        return 0;
                    });
                    tariffsObservable.setValue(tariffs);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<List<Operator>> getOperators() {
        return operatorsObservable;
    }

    public void refreshOperators() {
        mJsonAPI.getAllOperators().enqueue(new Callback<List<Operator>>() {
            @Override
            public void onResponse(@NonNull Call<List<Operator>> call, @NonNull Response<List<Operator>> response) {
                List<Operator> operators = response.body();
                if (operators != null) {
                    operatorsObservable.setValue(operators);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Operator>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
