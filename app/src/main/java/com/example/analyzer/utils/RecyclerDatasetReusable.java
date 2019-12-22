package com.example.analyzer.utils;

public class RecyclerDatasetReusable {
    private final String name;
    private final String phone;
    private final String date;

    public RecyclerDatasetReusable(String name, String phone, String date) {
        this.name = name;
        this.phone = phone;
        this.date = date;
    }

    String getName() {
        return this.name;
    }

    String getPhone() {
        return this.phone;
    }

    String getDate() {
        return this.date;
    }
}
