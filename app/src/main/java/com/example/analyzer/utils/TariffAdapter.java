package com.example.analyzer.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;

import java.util.List;

public class TariffAdapter extends RecyclerView.Adapter<TariffViewholder> {
    private final List<TariffDataset> data;

    public TariffAdapter(@NonNull List<TariffDataset> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public TariffViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tariffs_item, parent, false);
        return new TariffViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffViewholder holder, int position) {
        holder.name.setText(String.valueOf(data.get(position).getName()));
        holder.gigabyte.setText(String.valueOf(data.get(position).getGigabytes()));
        holder.sms.setText(String.valueOf(data.get(position).getSms()));
        holder.price.setText(String.valueOf(data.get(position).getPrice()));

        switch (data.get(position).getIcon()) {
            case 1:
                holder.icon.setImageResource(R.drawable.yota);
                break;
            case 2:
                holder.icon.setImageResource(R.drawable.mtc);
                break;
            case 3:
                holder.icon.setImageResource(R.drawable.beeline);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}