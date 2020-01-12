package com.example.analyzer.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;
import com.example.analyzer.service.model.TariffDataset;

import java.util.List;

public final class TariffAdapter extends RecyclerView.Adapter<TariffViewHolder> {
    public static final String YOTA = "yota";
    public static final String MTC = "mtc";
    public static final String BEELINE = "beeline";
    private final List<TariffDataset> items;
//    private final MainScreenFragment.EventListener eventListener;

    public TariffAdapter(@NonNull List<TariffDataset> data) {
        this.items = data;
    }

    @NonNull
    @Override
    public TariffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tariffs_item, parent, false);
        return new TariffViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffViewHolder holder, int position) {
        holder.name.setText(items.get(position).getName());
        holder.gigabyte.setText(items.get(position).getGigabytes() + " ГБ");
        holder.sms.setText(items.get(position).getSms() + " смс");
        holder.price.setText(items.get(position).getPrice() + " р/мес");

        switch (items.get(position).getIcon()) {
            case 2:
                holder.icon.setImageResource(R.drawable.yota);
                holder.icon.setContentDescription(YOTA);
                break;
            case 1:
                holder.icon.setImageResource(R.drawable.mtc);
                holder.icon.setContentDescription(MTC);
                break;
            case 0:
                holder.icon.setImageResource(R.drawable.beeline);
                holder.icon.setContentDescription(BEELINE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}