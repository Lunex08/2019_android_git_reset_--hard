package com.example.analyzer.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.TrafficStats;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.analyzer.R;
import com.example.analyzer.service.model.CallHistoryRecord;
import com.example.analyzer.service.repository.BalanceRepository;
import com.example.analyzer.service.repository.CallsRepository;
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

public class MainScreenViewModel extends AndroidViewModel {
    private final LiveData<List<CallHistoryRecord>> mCallsListObservable;
    private final LiveData<String> mBalance;

    private static final Float BYTES_TO_GIGS = 1024f * 1024f * 1024f;
    private static final String GIGS_FORMAT = "%.2f Гб";

    public MainScreenViewModel(@NonNull Application application) {
        super(application);
        mCallsListObservable = CallsRepository.getInstance().getCalls();
        loadCalls();

        mBalance = BalanceRepository.getInstance().getBalance();
    }

    public LiveData<List<BarEntry>> getCalls() {
        Log.d("ViewModel", String.valueOf(mCallsListObservable.getValue().size()));
        return Transformations.map(mCallsListObservable, this::transformData);
    }

    public LiveData<String> getBalance() {
        return mBalance;
    }

    public void refreshBalance() {
        BalanceRepository.getInstance().refreshBalance(getApplication().getApplicationContext());
    }

    public void loadCalls() {
        CallsRepository.getInstance().loadCalls(getApplication().getApplicationContext());
    }

    private List<BarEntry> transformData(List<CallHistoryRecord> callHistoryRecords) {
        final List<BarEntry> graphData = new ArrayList<>();

        final Date c = Calendar.getInstance().getTime();
        final String day = (String) DateFormat.format("dd", c);
        final String month = (String) DateFormat.format("MM", c);
        final String year = (String) DateFormat.format("yyyy", c);

        final int lastWeekDay = getApplication().getResources().getInteger(R.integer.LAST_WEEK_DAY);
        final int firstWeekDay = getApplication().getResources().getInteger(R.integer.FIRST_WEEK_DAY);

        if (callHistoryRecords != null) {
            int position = 0;

            for (int i = lastWeekDay; i >= firstWeekDay; --i) {
                final String newDay = String.valueOf(Integer.parseInt(day) - i);
                int countOfCalls = 0;

                for (CallHistoryRecord record : callHistoryRecords) {
                    final String dayRecord = (String) DateFormat.format("dd", record.getDate());
                    final String monthRecord = (String) DateFormat.format("MM", record.getDate());
                    final String yearRecord = (String) DateFormat.format("yyyy", record.getDate());

                    if (newDay.equals(dayRecord) && month.equals(monthRecord) && year.equals(yearRecord)) {
                        countOfCalls++;
                    }
                }

                graphData.add(new BarEntry(position, countOfCalls));
                position++;
            }
        }

        return graphData;
    }

    public void displayChart(BarChart barChart, Context ctx, List<BarEntry> data) {
        barChart.getLegend().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0f);

        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setEnabled(false);

        final BarDataSet barDataSet = new BarDataSet(data, "");
        barDataSet.setColors(ContextCompat.getColor(ctx, R.color.colorBars));
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

    @SuppressLint("DefaultLocale")
    public String getRemainTraffic() {
        return String.format(GIGS_FORMAT, TrafficStats.getMobileRxBytes() / BYTES_TO_GIGS);
    }

    public String getPhoneAddress() {
        //        SharedPreferences sp = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        //        String phoneNumberStr = sp.getString("phoneNumber", getResources().getString(R.string
        //        .number_not_rec));
        //        if ("".equals(phoneNumberStr)) {
        //            phoneNumberStr = getResources().getString(R.string.number_not_rec);
        //        }
        return "";
    }

    public String getOperatorName() {
        //        String operatorName = sp.getString("operatorName", getResources().getString(R.string
        //        .operator_not_rec));
        //        if ("".equals(operatorName)) {
        //            operatorName = getResources().getString(R.string.operator_not_rec);
        //        }
        return "";
    }
}
