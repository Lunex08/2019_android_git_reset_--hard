package com.example.analyzer.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;

import java.util.List;

public class RecyclerAdapterReusable extends RecyclerView.Adapter<RecyclerViewHolderReusable> {
    private final List<Integer> values;

    public RecyclerAdapterReusable(@NonNull List<Integer> values) {
        this.values = values;
    }

    @NonNull
    @Override
    public RecyclerViewHolderReusable onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_details_content_item, parent, false);
        return new RecyclerViewHolderReusable(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolderReusable holder, int position) {
        holder.singleValue.setText(String.valueOf(values.get(position)));
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
