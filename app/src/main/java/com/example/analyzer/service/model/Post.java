package com.example.analyzer.service.model;

import android.graphics.Color;

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
    @SerializedName("color")
    private String color;

    public String getColor() {return color; }

    public int getColorInt() {
        return Color.parseColor(color);
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    public Integer getId() {
        return id;
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

    public Post(String name, String traffic, String sms, double price, Integer id, String operator, String color) {
        this.name = name;
        this.traffic = traffic;
        this.sms = sms;
        this.price = price;
        this.id = id;
        this.operator = operator;
        this.color = color;
    }
}