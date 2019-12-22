package com.example.analyzer.view.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.analyzer.R;
import com.example.analyzer.viewmodel.MainScreenViewModel;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
import java.util.List;

public final class MainScreenFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "MainScreenFragmentTag";
    private static final String CALLS_MAP_KEY = "calls_number";
    private MainScreenViewModel viewModel;

    private List<Integer> callsNumber;
    private EventListener eventListener;

    public interface EventListener {
        void onItemClick(int dest);
    }

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(CALLS_MAP_KEY, (ArrayList<Integer>) callsNumber);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            callsNumber = savedInstanceState.getIntegerArrayList(CALLS_MAP_KEY);
        } else {
            callsNumber = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.main_screen_fragment, container, false);

        viewModel = new ViewModelProvider(getActivity()).get(MainScreenViewModel.class);

        final Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            Log.d(TAG, "Toolbar menu clicked");
            return true;
        });

        TextView traffic = v.findViewById(R.id.traffic);
        traffic.setText(viewModel.getRemainTraffic());

        String phoneAddressValue = viewModel.getPhoneAddress();
        TextView phoneAddress = v.findViewById(R.id.number);
        phoneAddress.setText(phoneAddressValue);

        TextView operatorTV = v.findViewById(R.id.operator);
        String operatorName = viewModel.getOperatorName();
        operatorTV.setText(operatorName);

        TextView balance = v.findViewById(R.id.balance);
        balance.setText("0"); //viewModel.getBalance()
        viewModel.getBalance().observe(getViewLifecycleOwner(), balanceValue -> {
            if (balance != null) {
                balance.setText(balanceValue);
            }
        });
        balance.setOnClickListener(v1 -> viewModel.refreshBalance());
        viewModel.refreshBalance();

        final Button tariffsButton = v.findViewById(R.id.title_tariffs_button);
        tariffsButton.setOnClickListener(this);

        final Button eventsButton = v.findViewById(R.id.title_events_button);
        eventsButton.setOnClickListener(this);

        final BarChart barChart = v.findViewById(R.id.main_graph);
        barChart.setOnClickListener(this);

        viewModel.getCalls().observe(getViewLifecycleOwner(), calls -> {
            if (calls != null) {
                viewModel.displayChart(barChart, getContext(), calls);
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_graph:
                eventListener.onItemClick(R.string.to_detail);
                break;
            case R.id.title_tariffs_button:
                eventListener.onItemClick(R.string.to_tariffs);
                break;
        }

    }
}
