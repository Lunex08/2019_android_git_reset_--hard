package com.example.analyzer.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.TrafficStats;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.analyzer.R;
import com.example.analyzer.service.model.CallHistoryRecord;
import com.example.analyzer.service.repository.BalanceRepository;
import com.example.analyzer.service.repository.CallsRepository;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainScreenViewModel extends AndroidViewModel {
    private final LiveData<List<CallHistoryRecord>> mCallsListObservable;
    private final LiveData<String> mBalance;
    private static final String GIGS_FORMAT = "%.2f Гб";
    private static final String BALANCE_CURRENCY = "%s₽";
    private static final Float BYTES_TO_GIGS = 1024f * 1024f * 1024f;
    public static final String MY_SETTINGS = "my_settings";

    public MainScreenViewModel(@NonNull Application application) {
        super(application);
        mCallsListObservable = CallsRepository.getInstance().getCalls();
        loadCalls();

        mBalance = BalanceRepository.getInstance().getBalance();
    }

    public LiveData<List<BarEntry>> getBarEtnriyCalls() {
        return Transformations.map(mCallsListObservable, this::transformData);
    }

    public List<CallHistoryRecord> getCalls() {
        return mCallsListObservable.getValue();
    }

    @SuppressLint("DefaultLocale")
    public LiveData<String> getBalance() {
       return Transformations.map(mBalance, value -> {
           SharedPreferences sp = getApplication().getApplicationContext().getSharedPreferences(MY_SETTINGS,
                   Context.MODE_PRIVATE);
           SharedPreferences.Editor e = sp.edit();
           // handle if error instead of balance value
           e.putFloat("balance", Float.valueOf(value));
           e.apply();
           return String.format(BALANCE_CURRENCY, value);
       });
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

    @SuppressLint("DefaultLocale")
    public String getRemainTraffic() {
        return String.format(GIGS_FORMAT, TrafficStats.getMobileRxBytes() / BYTES_TO_GIGS);
    }

    public String getPhoneAddress() {
        SharedPreferences sp = getApplication().getApplicationContext().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        return sp.getString("phoneNumber",
                getApplication().getApplicationContext().getResources().getString(R.string.number_not_rec));
    }

    public String getOperatorName() {
        SharedPreferences sp = getApplication().getApplicationContext().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        return sp.getString("operatorName",
                getApplication().getApplicationContext().getResources().getString(R.string.operator_not_rec));
    }

    public String getTarifName() {
        SharedPreferences sp = getApplication().getApplicationContext().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        String tariffName = sp.getString("tariffName",
                getApplication().getApplicationContext().getResources().getString(R.string.tariff_not_rec));
        if ("".equals(tariffName)) {
            tariffName = getApplication().getApplicationContext().getResources().getString(R.string.tariff_not_rec);
        }
        return tariffName;
    }
}
