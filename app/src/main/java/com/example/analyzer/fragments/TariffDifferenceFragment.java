package com.example.analyzer.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.analyzer.MainActivity;
import com.example.analyzer.R;
import com.example.analyzer.utils.TariffAdapter;
import com.example.analyzer.utils.TariffDataset;


public class TariffDifferenceFragment extends Fragment {
    private MainScreenFragment.EventListener eventListener;
    private TariffDataset dataset;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        eventListener = (MainScreenFragment.EventListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tariff_difference, container, false);

        final Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.tariffs);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            eventListener.onItemClick(R.string.to_settings);
            return true;
        });

        SharedPreferences sp = getActivity().getSharedPreferences(MainScreenFragment.MY_SETTINGS, Context.MODE_PRIVATE);
        String tariffName = sp.getString("tariffName", getResources().getString(R.string.tariff_not_rec));
        String operatorName = sp.getString("operatorName", getResources().getString(R.string.operator_not_rec));

        for (int i = 0; i < TariffsFragment.data.size(); i++) {
            if (TariffsFragment.data.get(i).getName().equals(tariffName)) {
                dataset = TariffsFragment.data.get(i);
                break;
            }
        }

        final TextView oper_left = view.findViewById(R.id.diff_oper_left);
        oper_left.setText(operatorName);

        final TextView tariff_left = view.findViewById(R.id.diff_tariff_left);
        tariff_left.setText(dataset.getName());

        final TextView gb_left = view.findViewById(R.id.diff_gb_left);
        gb_left.setText(dataset.getGigabytes() + " ГБ");

        final TextView sms_left = view.findViewById(R.id.diff_sms_left);
        sms_left.setText(dataset.getSms() + " смс");

        final TextView price_left = view.findViewById(R.id.diff_price_left);
        price_left.setText(dataset.getPrice() + " р/мес");

        final ImageView icon_left = view.findViewById(R.id.diff_oper_icon_left);

        switch (dataset.getIcon()) {
            case 2:
                icon_left.setImageResource(R.drawable.yota);
                break;
            case 1:
                icon_left.setImageResource(R.drawable.mtc);
                break;
            case 0:
                icon_left.setImageResource(R.drawable.beeline);
                break;
        }

        final Bundle bundle = getArguments();
        if (bundle != null) {
            final String name = bundle.getString(MainActivity.NAME);
            final String gigabyte = bundle.getString(MainActivity.GIGABYTE);
            final String sms = bundle.getString(MainActivity.SMS);
            final String price = bundle.getString(MainActivity.PRICE);
            final String icon = bundle.getString(MainActivity.ICON);

            final TextView oper_right = view.findViewById(R.id.diff_oper_right);
            final ImageView icon_right = view.findViewById(R.id.diff_oper_icon_right);

            assert icon != null;
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
