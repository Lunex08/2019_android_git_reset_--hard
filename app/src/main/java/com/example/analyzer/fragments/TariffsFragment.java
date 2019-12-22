package com.example.analyzer.fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.analyzer.R;
import com.example.analyzer.utils.JSONPlaceHolderApi;
import com.example.analyzer.utils.NetworkService;
import com.example.analyzer.utils.Post;
import com.example.analyzer.utils.TariffAdapter;
import com.example.analyzer.utils.TariffDataset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;


public final class TariffsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tariffs, container, false);

        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tariffs);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        ProgressBar bar = view.findViewById(R.id.wait_id);

        bar.setVisibility(View.VISIBLE);

        List<TariffDataset> data = new ArrayList<>();

        final RecyclerView recyclerView = view.findViewById(R.id.tariffs_content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final TariffAdapter recyclerAdapter = new TariffAdapter(data);
        recyclerView.setAdapter(recyclerAdapter);

        NetworkService.getInstance()
                .getJSONApi()
                .getAllPosts()
                .enqueue(new Callback<List<Post>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                        List<Post> posts = response.body();

                        if (posts != null) {
                            for (Post post : posts) {
                                bar.setVisibility(View.INVISIBLE);
                                Double price = BigDecimal.valueOf(post.getPrice())
                                        .setScale(2, RoundingMode.HALF_UP)
                                        .doubleValue();

                                data.add(new TariffDataset(post.getName(), post.getTraffic(), post.getSms(), String.valueOf(price), post.getId()));
                                recyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });

        return view;
    }

}