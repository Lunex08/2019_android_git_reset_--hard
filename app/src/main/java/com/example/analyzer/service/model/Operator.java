package com.example.analyzer.service.model;

import android.graphics.Color;

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
    @SerializedName("color")
    private String color;

    public String getColor() {return color; }

    public int getColorInt() {
        return Color.parseColor(color);
    }

    @NonNull
    public String getOperator() {
        return operator;
    }

    @NonNull
    public Integer getId() {
        return id;
    }

    public Operator(String operator, Integer id, String color) {
        this.operator = operator;
        this.id = id;
        this.color = color;
    }
}