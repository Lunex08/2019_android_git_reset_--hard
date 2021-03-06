package com.example.analyzer.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.analyzer.R;
import com.example.analyzer.modules.DataModule.CallHistoryRecord;
import com.example.analyzer.modules.DataModule.CallsModule;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MainScreenFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "MainScreenFragmentTag";
    private static final String CALLS_MAP_KEY = "calls_number";
    private static final String GIGS_FORMAT = "%.2f Гб";
    private static final String BALANCE_FORMAT = "%.2f₽";
    private static final String USER_SPECIFIC_USSD_GET_BALANCE = "*100#";
    private static final String USER_SPECIFIC_USSD_GET_NUMBER = "*103#";
    public static final String MY_SETTINGS = "my_settings";
    private static final Float BYTES_TO_GIGS = 1024f * 1024f * 1024f;
    private List<Integer> callsNumber;
    private EventListener eventListener;

    public interface EventListener {
        void onItemClick(int dest);

        void onTariffClick(String name, String gigabyte, String sms, String price, String icon);
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

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.main_screen_fragment, container, false);

        final Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            eventListener.onItemClick(R.string.to_settings);
            return true;
        });

        // Count traffic used since phone powered on
        TextView traffic = v.findViewById(R.id.traffic);
        float gigs = TrafficStats.getMobileRxBytes() / BYTES_TO_GIGS;
        traffic.setText(String.format(GIGS_FORMAT, gigs));

        // Get phone number
        TextView number = v.findViewById(R.id.number);
        SharedPreferences sp = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        String phoneNumberStr = sp.getString("phoneNumber", getResources().getString(R.string.number_not_rec));
        if ("".equals(phoneNumberStr)) {
            phoneNumberStr = getResources().getString(R.string.number_not_rec);
        }
        number.setText(phoneNumberStr);

        String operatorName = sp.getString("operatorName", getResources().getString(R.string.operator_not_rec));
        if ("".equals(operatorName)) {
            operatorName = getResources().getString(R.string.operator_not_rec);
        }
        TextView operatorTV = v.findViewById(R.id.operator);
        operatorTV.setText(operatorName);

        String tariffName = sp.getString("tariffName", getResources().getString(R.string.tariff_not_rec));
        if ("".equals(tariffName)) {
            tariffName = getResources().getString(R.string.tariff_not_rec);
        }
        TextView tariff = v.findViewById(R.id.tariff_value);
        tariff.setText(tariffName);

        TelephonyManager telephonyManager =
                (TelephonyManager) Objects.requireNonNull(getContext()).getSystemService(Context.TELEPHONY_SERVICE);

        TextView balance = v.findViewById(R.id.balance);
        float balanceValue = sp.getFloat("balance", 0.0f);
        String balanceValueString = balanceValue != 0 ? String.format(BALANCE_FORMAT, balanceValue): "touch to update";
        balance.setText(balanceValueString);
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager.UssdResponseCallback balanceCallback = new TelephonyManager.UssdResponseCallback() {
                    @Override
                    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request,
                                                      CharSequence response) {
                        super.onReceiveUssdResponse(telephonyManager, request, response);
                        final Pattern balancePattern = Pattern.compile("\\d+(.\\d+)?");

                        Matcher matcher = balancePattern.matcher(response.toString());
                        if (matcher.find()) {
                            String rawNumber = matcher.group(0);
                            rawNumber = rawNumber != null ? rawNumber.replace(",", ".") : "0";
                            if (response.toString().contains("Минус")) {
                                rawNumber = "-" + rawNumber;
                            }

                            float f = Float.parseFloat(rawNumber);
                            balance.setText(String.format(BALANCE_FORMAT, f));
                            SharedPreferences sp = getActivity().getSharedPreferences(MY_SETTINGS,
                                    Context.MODE_PRIVATE);
                            SharedPreferences.Editor e = sp.edit();
                            e.putFloat("balance", f);
                            e.apply();
                        }
                    }

                    @Override
                    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request,
                                                            int failureCode) {
                        super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                        Toast.makeText(getActivity(), String.valueOf(failureCode), Toast.LENGTH_SHORT).show();
                    }
                };

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    telephonyManager.sendUssdRequest(USER_SPECIFIC_USSD_GET_BALANCE, balanceCallback, new Handler());
                }
            }
        });

        final Button tariffsButton = v.findViewById(R.id.title_tariffs_button);
        tariffsButton.setOnClickListener(this);

        final BarChart barChart = v.findViewById(R.id.main_graph);
        buildBarChart(barChart, v);
        barChart.setOnClickListener(this);

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
        final List<BarEntry> graphData = new ArrayList<>();

        final Date c = Calendar.getInstance().getTime();
        final String day = (String) DateFormat.format("dd", c);
        final String month = (String) DateFormat.format("MM", c);
        final String year = (String) DateFormat.format("yyyy", c);

        final int lastWeekDay = getResources().getInteger(R.integer.LAST_WEEK_DAY);
        final int firstWeekDay = getResources().getInteger(R.integer.FIRST_WEEK_DAY);

        if (getActivity() != null) {
            CallsModule callsModule = new CallsModule(getActivity());
            List<CallHistoryRecord> callHistoryRecords = callsModule.getCalls();
            if (callHistoryRecords != null) {
                int position = 0;

                for (int i = lastWeekDay; i >= firstWeekDay; --i) {
                    final String newDay = String.valueOf(Integer.parseInt(day) - i);
                    int countOfCalls = 0;

                    for (CallHistoryRecord record : callHistoryRecords) {
                        final String dayRecord = ((String) DateFormat.format("dd", record.getDate())).replaceFirst(
                                "[0*]", "");
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
        }

        return graphData;
    }
}
