package com.example.analyzer.service.utils;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;

class RecyclerViewHolderReusable extends RecyclerView.ViewHolder {
    final TextView name;
    final TextView phone;
    final TextView date;


    RecyclerViewHolderReusable(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.content_name);
        phone = itemView.findViewById(R.id.content_phone);
        date = itemView.findViewById(R.id.content_date);
    }
}