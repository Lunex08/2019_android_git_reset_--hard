package com.example.analyzer.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;

class RecyclerViewHolderReusable extends RecyclerView.ViewHolder {
    final TextView singleValue;

    RecyclerViewHolderReusable(@NonNull View itemView) {
        super(itemView);
        singleValue = itemView.findViewById(R.id.content_name);
    }
}