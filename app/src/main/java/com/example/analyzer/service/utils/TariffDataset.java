package com.example.analyzer.service.utils;

import androidx.annotation.NonNull;

public class TariffDataset {
    private final String name;
    private final String gigabytes;
    private final String sms;
    private final String price;
    private final Integer icon;

    public TariffDataset(@NonNull String name, @NonNull String gigabytes, @NonNull String sms, @NonNull String price, @NonNull Integer icon) {
        this.name = name;
        this.gigabytes = gigabytes;
        this.sms = sms;
        this.price = price;
        this.icon = icon;
    }

    @NonNull String getName() {
        return this.name;
    }

    @NonNull String getGigabytes() {
        return this.gigabytes;
    }

    @NonNull String getSms() {
        return this.sms;
    }

    @NonNull String getPrice() { return this.price; }

    @NonNull Integer getIcon() { return this.icon; }
}
