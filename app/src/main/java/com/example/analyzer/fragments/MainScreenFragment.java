package com.example.analyzer.fragments;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.analyzer.R;

public class MainScreenFragment extends Fragment implements View.OnClickListener {
    final static public String TAG = "MainScreenFragmentTag";

    public static MainScreenFragment getInstance() {
        return new MainScreenFragment();
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

        TextView balance = (TextView) v.findViewById(R.id.balance);
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);

            TelephonyManager.UssdResponseCallback responseCallback = new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    super.onReceiveUssdResponse(telephonyManager, request, response);
                    Log.d("USSD resp ok: ", response.toString());
                    Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_SHORT).show();
                    // Пропарсить response
                    // Получить численное значение баланса и вывести
//                    balance.setText(String.format("%.2f rub", balance.get())); не робит. создать переменную класса для хранения
                }

                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                    Log.d("USSD resp fail: ", String.valueOf(failureCode));
                    Toast.makeText(getActivity(), String.valueOf(failureCode), Toast.LENGTH_SHORT).show();
                }
            };

            TelephonyManager telephonyManager = (TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE);
            telephonyManager.sendUssdRequest("*102#", responseCallback, new Handler());
            }
        });

        return v;
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Tarif cardView clicked");
    }
}
