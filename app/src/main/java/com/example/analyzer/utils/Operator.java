package com.example.analyzer.utils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Operator {
    @SerializedName("operator")
    private String operator;
    @SerializedName("operator_id")
    private Integer id;

    @NonNull
    public String getOperator() {
        return operator;
    }

    @NonNull
    public Integer getId() {
        return id;
    }
}
