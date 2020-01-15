package com.example.analyzer.view.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.analyzer.R;
import com.example.analyzer.service.model.EventListener;
import com.example.analyzer.service.model.TariffDataset;

import java.util.List;

public class TariffDifferenceFragment extends Fragment {
    private EventListener eventListener;
    private List<TariffDataset> tariffs;
    private TariffDataset currentTariff;
    public static final String MY_SETTINGS = "my_settings";

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

    public void setTariffs(List<TariffDataset> tariffs) {
        this.tariffs = tariffs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tariff_difference, container, false);

        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tariffs);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        SharedPreferences sp1 = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        int clr = Color.parseColor(sp1.getString("color", "#008577"));
        toolbar.setBackgroundColor(clr);

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            eventListener.showInfoFragment();
            return true;
        });


        SharedPreferences sp = getActivity().getSharedPreferences(MainScreenFragment.MY_SETTINGS, Context.MODE_PRIVATE);
        String tariffName = sp.getString("tariffName", getResources().getString(R.string.tariff_not_rec));
        String operatorName = sp.getString("operatorName", getResources().getString(R.string.operator_not_rec));

        for (int i = 0; i < tariffs.size(); i++) {
            if (tariffs.get(i).getName().equals(tariffName)) {
                currentTariff = tariffs.get(i);
                break;
            }
        }

        final TextView oper_left = view.findViewById(R.id.diff_oper_left);
        oper_left.setText(operatorName);

        final TextView tariff_left = view.findViewById(R.id.diff_tariff_left);
        tariff_left.setText(currentTariff.getName());

        final TextView gb_left = view.findViewById(R.id.diff_gb_left);
        gb_left.setText(currentTariff.getGigabytes() + " ГБ");

        final TextView sms_left = view.findViewById(R.id.diff_sms_left);
        sms_left.setText(currentTariff.getSms() + " смс");

        final TextView price_left = view.findViewById(R.id.diff_price_left);
        price_left.setText(currentTariff.getPrice() + " р/мес");

        final View icon_left = view.findViewById(R.id.diff_oper_icon_left);
        icon_left.setBackgroundColor(currentTariff.getColor());

        final Bundle bundle = getArguments();
        if (bundle != null) {
            final String name = bundle.getString(MainActivity.NAME);
            final String gigabyte = bundle.getString(MainActivity.GIGABYTE);
            final String sms = bundle.getString(MainActivity.SMS);
            final String price = bundle.getString(MainActivity.PRICE);
            final String icon = bundle.getString(MainActivity.ICON);
            final int color = bundle.getInt(MainActivity.COLOR);

            final View icon_right = view.findViewById(R.id.diff_oper_icon_right);
            icon_right.setBackgroundColor(color);

            final TextView oper_right = view.findViewById(R.id.diff_oper_right);
            assert icon != null;
            oper_right.setText(icon);

            final TextView tariff_right = view.findViewById(R.id.diff_tariff_right);
            tariff_right.setText(name);

            final TextView gb_right = view.findViewById(R.id.diff_gb_right);
            gb_right.setText(gigabyte);

            final TextView sms_right = view.findViewById(R.id.diff_sms_right);
            sms_right.setText(sms);

            final TextView price_right = view.findViewById(R.id.diff_price_right);
            price_right.setText(price);

            final TextView profit = view.findViewById(R.id.diff_profit_value);

            final int chars_pos = 8;

            final Double integer_left_price = Double.valueOf(price_left.getText().toString().substring(0,
                    price_left.getText().toString().length() - chars_pos));
            final Double integer_right_price = Double.valueOf(price_right.getText().toString().substring(0,
                    price_right.getText().toString().length() - chars_pos));


            final double difference = integer_left_price - integer_right_price;
            if (difference > 0) {
                profit.setText(difference + " р/мес");
            } else {
                profit.setText(getResources().getString(R.string.no_profit));
            }
        }

        return view;
    }


}