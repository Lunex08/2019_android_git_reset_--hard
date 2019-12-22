package com.example.analyzer.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.analyzer.R;
import com.example.analyzer.modules.CallsModule.CallHistoryRecord;
import com.example.analyzer.modules.CallsModule.CallsModule;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainScreenFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "MainScreenFragmentTag";
    private static final String CALLS_MAP_KEY = "calls_number";
    private static final String GIGS_FORMAT = "%.2f Гб";
    private static final String BALANCE_FORMAT = "%.2f₽";
    private static final String USER_SPECIFIC_USSD_GET_BALANCE = "*100#";
    private static final String USER_SPECIFIC_USSD_GET_NUMBER = "*103#";
    private static final Float BYTES_TO_GIGS = 1024f * 1024f * 1024f;
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
            Log.d(TAG, "Toolbar menu clicked");
            return true;
        });


        //final CardView cardView = v.findViewById(R.id.card_view);
        //cardView.setOnClickListener(this);

        TextView traffic = (TextView) v.findViewById(R.id.traffic); // Считаем трафик
        float gigs = TrafficStats.getMobileRxBytes() / BYTES_TO_GIGS;
        traffic.setText(String.format(GIGS_FORMAT, gigs));

        // Узнаем номер мобилки (!не всегда работает!)
        TextView number = (TextView) v.findViewById(R.id.number);

        TelephonyManager telephonyManager = (TelephonyManager) Objects.requireNonNull(getContext()).getSystemService(Context.TELEPHONY_SERVICE);

        TelephonyManager.UssdResponseCallback numberCallback = new TelephonyManager.UssdResponseCallback() {
            @Override
            public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                super.onReceiveUssdResponse(telephonyManager, request, response);
                final Pattern numberPattern = Pattern.compile("\\+\\d{11}?");
                Matcher matcher = numberPattern.matcher(response.toString());
                if (matcher.find()) {
                    String res = matcher.group(0);
                    number.setText(res);
                }
            }

            @Override
            public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                Toast.makeText(getActivity(), String.valueOf(failureCode), Toast.LENGTH_SHORT).show();
            }
        };

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.CALL_PHONE}, 14);
        }
        assert telephonyManager != null;
        telephonyManager.sendUssdRequest(USER_SPECIFIC_USSD_GET_NUMBER, numberCallback, new Handler());

        TextView balance = (TextView) v.findViewById(R.id.balance);
        balance.setText(String.format(BALANCE_FORMAT, 0.0f));
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TelephonyManager.UssdResponseCallback balanceCallback = new TelephonyManager.UssdResponseCallback() {
                    @Override
                    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                        super.onReceiveUssdResponse(telephonyManager, request, response);
                        final Pattern balancePattern = Pattern.compile("\\d+(.\\d+)?");
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                        Matcher matcher = balancePattern.matcher(response.toString());
                        if (matcher.find()) {
                            String rawnumber = matcher.group(0);
                            Float f = Float.parseFloat(rawnumber != null ? rawnumber : "0");
                            balance.setText(String.format(BALANCE_FORMAT, f ));
                        }
                    }

                    @Override
                    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                        super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                        Log.d("USSD resp fail: ", request + String.valueOf(failureCode));
                        Toast.makeText(getActivity(), String.valueOf(failureCode), Toast.LENGTH_SHORT).show();
                    }
                };

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CALL_PHONE}, 13);
                }
                telephonyManager.sendUssdRequest(USER_SPECIFIC_USSD_GET_BALANCE, balanceCallback,  new Handler());
            }
        });

        final Button tariffsButton = v.findViewById(R.id.title_tariffs_button);
        tariffsButton.setOnClickListener(this);

        final Button eventsButton = v.findViewById(R.id.title_events_button);
        eventsButton.setOnClickListener(this);

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
                        final String dayRecord = (String) DateFormat.format("dd", record.getDate());

                        if (newDay.equals(dayRecord)) {
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
