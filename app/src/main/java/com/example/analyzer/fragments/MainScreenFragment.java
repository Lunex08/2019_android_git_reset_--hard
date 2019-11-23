package com.example.analyzer.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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
    final public static String TAG = "MainScreenFragmentTag";
    final private static String CALLS_MAP_KEY = "calls_number";
    private List<Integer> callsNumber;

    public static MainScreenFragment getInstance() {
        return new MainScreenFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(CALLS_MAP_KEY, (ArrayList<Integer>)callsNumber);
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

        BarChart barChart = v.findViewById(R.id.main_graph);
        buildBarChart(barChart);

        return v;
    }

    public void onClick(View v) {
        Log.d(TAG, "Tarif cardView clicked");
    }

    private void buildBarChart(BarChart barChart) {
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0f);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setEnabled(false);

        ArrayList<BarEntry> data = getData();

        BarDataSet barDataSet = new BarDataSet(data, "");
        barDataSet.setColors(Color.RED);
        barDataSet.setDrawValues(false);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.5f);
        barChart.setData(barData);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        Date c = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd",   c);
        String month = (String) DateFormat.format("MM", c);

        ArrayList<String> datesList = new ArrayList<>();

        for (int i = 6; i >= 0; --i) {
            String newDay = (Integer.parseInt(day) - i) + "." + month;
            datesList.add(newDay);
        }

        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(datesList);
        xAxis.setValueFormatter(formatter);
    }

    private ArrayList<BarEntry> getData(){
        // Fill graphData here (7 days = 7 times add) using callsNumber, e.g:
        // graphData.add(new BarEntry(0f, 15f));

        // For test
        ArrayList<BarEntry> graphData = new ArrayList<>();

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
