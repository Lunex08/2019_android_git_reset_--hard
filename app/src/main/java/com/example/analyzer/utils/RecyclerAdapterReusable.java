package com.example.analyzer.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;


import java.util.List;

public class RecyclerAdapterReusable extends RecyclerView.Adapter<RecyclerViewHolderReusable> {
    public static final String unknown_number = "Неизвестный";
    private final List<RecyclerDatasetReusable> data;

    public RecyclerAdapterReusable(@NonNull List<RecyclerDatasetReusable> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerViewHolderReusable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_details_content_item, parent, false);
        return new RecyclerViewHolderReusable(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderReusable holder, int position) {
        if (data.get(position).getName() == null) {
            holder.name.setText(unknown_number);
        } else {
            holder.name.setText(String.valueOf(data.get(position).getName()));
        }

        holder.phone.setText(String.valueOf(data.get(position).getPhone()));
        holder.date.setText(String.valueOf(data.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
