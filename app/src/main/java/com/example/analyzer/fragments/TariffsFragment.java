package com.example.analyzer.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.analyzer.R;
import com.example.analyzer.utils.NetworkService;
import com.example.analyzer.utils.Post;
import com.example.analyzer.utils.TariffAdapter;
import com.example.analyzer.utils.TariffDataset;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public final class TariffsFragment extends Fragment {
    public static List<TariffDataset> data;
    private MainScreenFragment.EventListener eventListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        eventListener = (MainScreenFragment.EventListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventListener = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tariffs, container, false);

        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tariffs);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            eventListener.onItemClick(R.string.to_settings);
            return true;
        });

        ProgressBar bar = view.findViewById(R.id.wait_id);

        bar.setVisibility(View.VISIBLE);

        data = new ArrayList<>();

        final RecyclerView recyclerView = view.findViewById(R.id.tariffs_content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final TariffAdapter recyclerAdapter = new TariffAdapter(data, eventListener);
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
                                        .setScale(0, RoundingMode.HALF_UP)
                                        .doubleValue();

                                data.add(new TariffDataset(post.getName(), post.getTraffic(), post.getSms(), String.valueOf(price), post.getId()));
                            }

                            Collections.sort(data, (o1, o2) -> {
                                double first = Double.valueOf(o1.getPrice());
                                double second = Double.valueOf(o2.getPrice());
                                if (first > second) return 1;
                                if (first < second) return -1;
                                return 0;
                            });

                            recyclerAdapter.notifyDataSetChanged();
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