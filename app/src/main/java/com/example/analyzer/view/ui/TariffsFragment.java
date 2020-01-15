package com.example.analyzer.view.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;
import com.example.analyzer.service.model.EventListener;
import com.example.analyzer.service.model.TariffDataset;
import com.example.analyzer.viewmodel.TariffsViewModel;

import java.util.ArrayList;
import java.util.List;


public final class TariffsFragment extends Fragment {
    public List<TariffDataset> tariffs;
    private EventListener eventListener;
    public static final String MY_SETTINGS = "my_settings";
    private TariffsViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        eventListener = (EventListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventListener = null;
    }

    public List<TariffDataset> getTariffs() {
        return tariffs;
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
        SharedPreferences sp1 = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        toolbar.setBackgroundColor(Color.parseColor(sp1.getString("color", "#008577")));


        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            eventListener.showInfoFragment();
            return true;
        });

        final ProgressBar bar = view.findViewById(R.id.wait_id);
        bar.setVisibility(View.VISIBLE);

        tariffs = new ArrayList<>();
        final TariffAdapter recyclerAdapter = new TariffAdapter(eventListener, tariffs);
        final RecyclerView recyclerView = view.findViewById(R.id.tariffs_content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerAdapter);

        viewModel = new ViewModelProvider(this).get(TariffsViewModel.class);
        viewModel.getTariffs().observe(getViewLifecycleOwner(), updatedTariffs -> {
            bar.setVisibility(View.INVISIBLE);
            if (tariffs != null) {
                tariffs.clear();
                tariffs.addAll(updatedTariffs);
                recyclerAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

}