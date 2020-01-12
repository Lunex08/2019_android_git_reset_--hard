package com.example.analyzer.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.analyzer.R;
import com.example.analyzer.service.model.TariffDataset;
import com.example.analyzer.view.ui.MainActivity;
import com.example.analyzer.view.ui.MainScreenFragment;

import java.util.List;

public class TariffDifferenceViewModel extends AndroidViewModel {
    private List<TariffDataset> tariffs;
    private TariffDataset currentTariff;
    SharedPreferences sp;

    public TariffDifferenceViewModel(@NonNull Application application) {
        super(application);
    }

    public void setTariffs(List<TariffDataset> tariffs) {
        this.tariffs = tariffs;
        init();
    }

    void init() {
        sp = getApplication().getSharedPreferences(MainScreenFragment.MY_SETTINGS, Context.MODE_PRIVATE);
        String tariffName = sp.getString("tariffName",
                getApplication().getResources().getString(R.string.tariff_not_rec));


        for (int i = 0; i < tariffs.size(); i++) {
            if (tariffs.get(i).getName().equals(tariffName)) {
                currentTariff = tariffs.get(i);
                break;
            }
        }
    }

    public TariffDataset getCurrentTariff() {
        return currentTariff;
    }

    public String getProfit(TariffDataset proposalTariff) {
        String profit;
        final int chars_pos = 8;
        final double integer_left_price = Double.valueOf(this.currentTariff.getPrice().replace("[^-?0-9]+", ""));
        final double integer_right_price = Double.valueOf(proposalTariff.getPrice().replace("[^-?0-9]+", ""));

        final double difference = integer_left_price - integer_right_price;
        if (difference > 0) {
            profit = difference + " р/мес";
        } else {
            profit = getApplication().getResources().getString(R.string.no_profit);
        }

        return profit;
    }

    public TariffDataset getProposeTariff(Bundle bundle) {
        if (bundle == null) {
            throw new NullPointerException("No proposal tariff data");
        }

        final String name = bundle.getString(MainActivity.NAME);
        final String gigabyte = bundle.getString(MainActivity.GIGABYTE).replaceAll("[^-?0-9]+", "");
        final String sms = bundle.getString(MainActivity.SMS).replaceAll("[^-?0-9]+", "");
        final String price = bundle.getString(MainActivity.PRICE).replaceAll("[^-?0-9]+", "");
        final String operator = bundle.getString(MainActivity.OPERATOR);

        return new TariffDataset(name, gigabyte, sms, price, operator);
    }
}
