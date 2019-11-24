package com.example.analyzer.fragments;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.analyzer.R;
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

public class MainScreenFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "MainScreenFragmentTag";
    private static final String CALLS_MAP_KEY = "calls_number";
    private List<Integer> callsNumber;

    public static MainScreenFragment getInstance() {
        return new MainScreenFragment();
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

        final Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle("Analyzer");
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            Log.d(TAG, "Toolbar menu clicked");
            return true;
        });


        final CardView cardView = v.findViewById(R.id.card_view);
        cardView.setOnClickListener(this);

        final Button tariffsButton = v.findViewById(R.id.title_tariffs_button);
        tariffsButton.setOnClickListener(this);

        final Button eventsButton = v.findViewById(R.id.title_events_button);
        eventsButton.setOnClickListener(this);

        final BarChart barChart = v.findViewById(R.id.main_graph);
        buildBarChart(barChart, v);
        barChart.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_graph:
                Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG).show();
                break;
        }

    }

    private void buildBarChart(BarChart barChart, View v) {
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0f);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setEnabled(false);

        final List<BarEntry> data = getData();

        final BarDataSet barDataSet = new BarDataSet(data, "");
        barDataSet.setColors(ContextCompat.getColor(v.getContext(), R.color.colorBars));
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

        final int lastWeekDay = getResources().getInteger(R.integer.LAST_WEEK_DAY);
        final int firstWeekDay = getResources().getInteger(R.integer.FIRST_WEEK_DAY);

        for (int i = lastWeekDay; i >= firstWeekDay; --i) {
            final String newDay = (Integer.parseInt(day) - i) + "." + month;
            datesList.add(newDay);
        }

        final IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(datesList);
        xAxis.setValueFormatter(formatter);
    }

    private List<BarEntry> getData() {
        // Fill graphData here (7 days = 7 times add) using callsNumber, e.g:
        // graphData.add(new BarEntry(0f, 15f));

        // For test
        final List<BarEntry> graphData = new ArrayList<>();

        graphData.add(new BarEntry(0f, 4f));
        graphData.add(new BarEntry(1f, 11f));
        graphData.add(new BarEntry(2f, 0f));
        graphData.add(new BarEntry(3f, 3f));
        graphData.add(new BarEntry(4f, 7f));
        graphData.add(new BarEntry(5f, 4f));
        graphData.add(new BarEntry(6f, 9f));

        return graphData;
    }
}
