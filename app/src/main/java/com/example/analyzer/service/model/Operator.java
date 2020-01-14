package com.example.analyzer.service.model;

import androidx.annotation.NonNull;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "operators")
public class Operator {
    @SerializedName("operator")
    private String operator;
    @PrimaryKey(autoGenerate = false)
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