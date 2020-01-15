package com.example.analyzer.service.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tariffs")
public class TariffDataset {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private final String name;
    private final String gigabytes;
    private final String sms;
    private final String price;
    private final String operator;
    private final Integer icon;
    private final Integer color;

    public TariffDataset(@NonNull String name,
                         @NonNull String gigabytes,
                         @NonNull String sms,
                         @NonNull String price,
                         @NonNull String operator,
                         @NonNull Integer icon,
                         @NonNull Integer color) {
        this.name = name;
        this.color = color;
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
    public Integer getColor() {
        return this.color;
    }

    @NonNull
    public String getGigabytes() {
        if ("Безлим.".equals(this.gigabytes)) {
            return this.gigabytes;
        }
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