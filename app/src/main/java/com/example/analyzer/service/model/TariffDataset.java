package com.example.analyzer.service.model;

import androidx.annotation.NonNull;

import androidx.room.Entity;

@Entity
public class TariffDataset {
    private final String name;
    private final String gigabytes;
    private final String sms;
    private final String price;
    private final String operator;
    private final Integer icon;

    public TariffDataset(@NonNull String name, @NonNull String gigabytes, @NonNull String sms, @NonNull String price,
                         @NonNull String operator,
                         @NonNull Integer icon) {
        this.name = name;
        this.gigabytes = gigabytes;
        this.sms = sms;
        this.price = price;
        this.operator = operator;
        this.icon = icon;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getGigabytes() {
        return (Double.valueOf(this.gigabytes) < 0 ? "Безлим." : this.gigabytes);
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

    @NonNull
    public Integer getIcon() {
        return this.icon;
    }
}