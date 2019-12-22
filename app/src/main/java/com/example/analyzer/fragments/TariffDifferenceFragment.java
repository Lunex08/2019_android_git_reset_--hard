package com.example.analyzer.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.analyzer.MainActivity;
import com.example.analyzer.R;
import com.example.analyzer.utils.TariffAdapter;


public class TariffDifferenceFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tariff_difference, container, false);

        //ХАРДКОД ЛЮТЫЙ ДЛЯ ТЕСТОВ

        final TextView oper_left = view.findViewById(R.id.diff_oper_left);
        oper_left.setText("МТС");

        final TextView tariff_left = view.findViewById(R.id.diff_tariff_left);
        tariff_left.setText("Безлимитный");

        final TextView gb_left = view.findViewById(R.id.diff_gb_left);
        gb_left.setText("20 ГБ");

        final TextView sms_left = view.findViewById(R.id.diff_sms_left);
        sms_left.setText("200 смс");

        final TextView price_left = view.findViewById(R.id.diff_price_left);
        price_left.setText("650.0 р/мес");

        final ImageView icon_left = view.findViewById(R.id.diff_oper_icon_left);
        icon_left.setImageResource(R.drawable.mtc);

        final Bundle bundle = getArguments();

        if (bundle != null) {
            final String name = bundle.getString(MainActivity.NAME);
            final String gigabyte = bundle.getString(MainActivity.GIGABYTE);
            final String sms = bundle.getString(MainActivity.SMS);
            final String price = bundle.getString(MainActivity.PRICE);
            final String icon = bundle.getString(MainActivity.ICON);

            final TextView oper_right = view.findViewById(R.id.diff_oper_right);
            final ImageView icon_right = view.findViewById(R.id.diff_oper_icon_right);

            switch (icon) {
                case TariffAdapter.MTC:
                    icon_right.setImageResource(R.drawable.mtc);
                    oper_right.setText(getResources().getString(R.string.MTC));
                    break;
                case TariffAdapter.YOTA:
                    icon_right.setImageResource(R.drawable.yota);
                    oper_right.setText(getResources().getString(R.string.Yota));
                    break;
                case TariffAdapter.BEELINE:
                    icon_right.setImageResource(R.drawable.beeline);
                    oper_right.setText(getResources().getString(R.string.Beeline));
                    break;
            }

            final TextView tariff_right = view.findViewById(R.id.diff_tariff_right);
            tariff_right.setText(name);

            final TextView gb_right = view.findViewById(R.id.diff_gb_right);
            gb_right.setText(gigabyte + " ГБ");

            final TextView sms_right = view.findViewById(R.id.diff_sms_right);
            sms_right.setText(sms + " смс");

            final TextView price_right = view.findViewById(R.id.diff_price_right);
            price_right.setText(price + " р/мес");

            final TextView profit = view.findViewById(R.id.diff_profit_value);

            final int chars_pos = 8;

            final Double integer_left_price = Double.valueOf(price_left.getText().toString().substring(0, price_left.getText().toString().length() - chars_pos));
            final Double integer_right_price = Double.valueOf(price_right.getText().toString().substring(0, price_right.getText().toString().length() - chars_pos));


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
