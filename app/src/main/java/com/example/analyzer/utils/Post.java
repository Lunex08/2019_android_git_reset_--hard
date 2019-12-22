package com.example.analyzer.utils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("tariff_name")
    private String name;
    @SerializedName("traffic")
    private String traffic;
    @SerializedName("sms")
    private String sms;
    @SerializedName("price")
    private Double price;
    @SerializedName("operator_id")
    private Integer id;

    @NonNull public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull public String getTraffic() {
        return traffic;
    }

    public void setTraffic(String traffic) {
        this.traffic = traffic;
    }

    @NonNull public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    @NonNull public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @NonNull public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
