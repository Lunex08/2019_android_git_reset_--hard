package com.example.analyzer.service.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("tariff")
    private String name;
    @SerializedName("traffic")
    private String traffic;
    @SerializedName("sms")
    private String sms;
    @SerializedName("price")
    private double price;
    @SerializedName("operator_id")
    private Integer id;
    @SerializedName("operator")
    private String operator;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    @NonNull
    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    @NonNull
    public double getPrice() {
        return price;
    }

    @NonNull
    public String getOperator() {
        return operator;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}