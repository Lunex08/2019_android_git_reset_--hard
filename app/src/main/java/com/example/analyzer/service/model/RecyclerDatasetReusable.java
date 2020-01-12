package com.example.analyzer.service.model;

import androidx.annotation.NonNull;

public class RecyclerDatasetReusable {
    private final String name;
    private final String phone;
    private final String date;

    public RecyclerDatasetReusable(@NonNull String name, @NonNull String phone, @NonNull String date) {
        this.name = name;
        this.phone = phone;
        this.date = date;
    }

    @NonNull
    public String getName() {
        return this.name;
    }

    @NonNull
    public String getPhone() {
        return this.phone;
    }

    @NonNull
    public String getDate() {
        return this.date;
    }
}