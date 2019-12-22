package com.example.analyzer.service.utils;

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

    @NonNull String getName() {
        return this.name;
    }

    @NonNull String getPhone() {
        return this.phone;
    }

    @NonNull String getDate() {
        return this.date;
    }
}
