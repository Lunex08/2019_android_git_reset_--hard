package com.example.analyzer.utils;

public class TariffDataset {
    private final String name;
    private final String gigabytes;
    private final String sms;
    private final String price;
    private final Integer icon;

    public TariffDataset(String name, String gigabytes, String sms, String price, Integer icon) {
        this.name = name;
        this.gigabytes = gigabytes;
        this.sms = sms;
        this.price = price;
        this.icon = icon;
    }

    String getName() {
        return this.name;
    }

    String getGigabytes() {
        return this.gigabytes;
    }

    String getSms() {
        return this.sms;
    }

    String getPrice() { return this.price; }

    Integer getIcon() { return this.icon; }
}
