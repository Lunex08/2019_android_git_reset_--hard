package com.example.analyzer.view.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.analyzer.R;
import com.example.analyzer.viewmodel.MainScreenViewModel;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;
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

        TextView balance = v.findViewById(R.id.balance);
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
        barChart.setOnClickListener(this);

        final MainScreenViewModel viewModel = new ViewModelProvider(getActivity()).get(MainScreenViewModel.class);
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
