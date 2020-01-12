package com.example.analyzer.service.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.analyzer.R;
import com.example.analyzer.view.ui.TariffAdapter;

public class TariffDataset {
    private final String name;
    private final String gigabytes;
    private final String sms;
    private final String price;
    private final String operator;

    public TariffDataset(@NonNull String name, @NonNull String gigabytes, @NonNull String sms, @NonNull String price,
                         @NonNull String operator) {
        this.name = name;
        this.gigabytes = gigabytes;
        this.sms = sms;
        this.price = price;
        this.operator = operator;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getGigabytes() {
        String value = this.gigabytes.replaceAll("[^0-9]", "");
        return (Double.valueOf(value) < 0 ? "Безлим." : value);
    }

    @NonNull
    public String getSms() {
        return this.sms;
    }

    @NonNull
    public String getPrice() {
        return this.price;
    }

    @NonNull
    public String getOperator() {
        return this.operator;
    }

    static public int getImageResource(String operator) {
        int imageResource = -1;
        switch (operator.toLowerCase()) {
            case TariffAdapter.YOTA:
                imageResource = R.drawable.yota;
                break;
            case TariffAdapter.MTC:
                imageResource = R.drawable.mtc;
                break;
            case TariffAdapter.BEELINE:
                imageResource = R.drawable.beeline;
                break;
        }
        if(imageResource == -1) {
            Log.d("bad", "bad");
        }
        return imageResource;
    }
}