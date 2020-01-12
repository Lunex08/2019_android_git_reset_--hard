package com.example.analyzer.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;
import com.example.analyzer.service.model.RecyclerDatasetReusable;

import java.util.List;

public class RecyclerAdapterReusable extends RecyclerView.Adapter<RecyclerViewHolderReusable> {
    public static final String unknown_number = "Неизвестный";
    private final List<RecyclerDatasetReusable> items;

    public RecyclerAdapterReusable(@NonNull List<RecyclerDatasetReusable> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerViewHolderReusable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_details_content_item, parent,
                false);
        return new RecyclerViewHolderReusable(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderReusable holder, int position) {
        if (items.get(position).getName() == null) {
            holder.name.setText(unknown_number);
        } else {
            holder.name.setText(items.get(position).getName());
        }

        holder.phone.setText(items.get(position).getPhone());
        holder.date.setText(items.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}