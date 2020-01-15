package com.example.analyzer.view.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.analyzer.R;
import com.example.analyzer.service.model.EventListener;
import com.example.analyzer.viewmodel.MainScreenViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public final class MainScreenFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "MainScreenFragmentTag";
    private static final String CALLS_MAP_KEY = "calls_number";
    public static final String MY_SETTINGS = "my_settings";
    private MainScreenViewModel viewModel;

    private List<Integer> callsNumber;
    private EventListener eventListener;

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

        viewModel = new ViewModelProvider(this).get(MainScreenViewModel.class);

        final Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);
        SharedPreferences sp1 = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);int clr = Color.parseColor(sp1.getString("color", "#008577"));
        toolbar.setBackgroundColor(clr);
        Window window = getActivity().getWindow();
        window.setStatusBarColor(clr);

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            eventListener.showInfoFragment();
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

        String tarifName = viewModel.getTarifName();
        TextView tariff = v.findViewById(R.id.tariff_value);
        tariff.setText(tarifName);

        TextView balance = v.findViewById(R.id.balance);
        balance.setText("Click to update");
        viewModel.getBalance().observe(getViewLifecycleOwner(), balanceValue -> {
            if (balanceValue != null) {
                balance.setText(balanceValue);
            }
        });
        balance.setOnClickListener(value -> viewModel.refreshBalance());
        viewModel.refreshBalance();

        final Button tariffsButton = v.findViewById(R.id.title_tariffs_button);
        tariffsButton.setOnClickListener(this);

        final BarChart barChart = v.findViewById(R.id.main_graph);
        barChart.setOnClickListener(this);
        viewModel.getBarEtnriyCalls().observe(getViewLifecycleOwner(), calls -> {
            if (calls != null) {
                displayChart(barChart, calls);
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_graph:
                eventListener.showDetailsFragment(viewModel.getCalls(), viewModel.getSms());
                break;
            case R.id.title_tariffs_button:
                eventListener.showTariffsFragment();
                break;
        }
    }

    public void displayChart(BarChart barChart, List<BarEntry> data) {
        Context ctx = Objects.requireNonNull(getContext());
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0f);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setEnabled(false);

        final BarDataSet barDataSet = new BarDataSet(data, "");
        barDataSet.setColors(ContextCompat.getColor(getContext(), R.color.colorBars));
        barDataSet.setDrawValues(false);

        final BarData barData = new BarData(barDataSet);
        barData.setHighlightEnabled(false);
        barData.setBarWidth(0.5f);
        barChart.setData(barData);

        final XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        final Date c = Calendar.getInstance().getTime();
        final String day = (String) DateFormat.format("dd", c);
        final String month = (String) DateFormat.format("MM", c);

        final List<String> datesList = new ArrayList<>();

        final int lastWeekDay = ctx.getResources().getInteger(R.integer.LAST_WEEK_DAY);
        final int firstWeekDay = ctx.getResources().getInteger(R.integer.FIRST_WEEK_DAY);

        for (int i = lastWeekDay; i >= firstWeekDay; --i) {
            final String newDay = (Integer.parseInt(day) - i) + "." + month;
            datesList.add(newDay);
        }

        final IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(datesList);
        xAxis.setValueFormatter(formatter);
    }
}
