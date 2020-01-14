package com.example.analyzer.view.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;
import com.example.analyzer.service.model.EventListener;
import com.example.analyzer.service.model.TariffDataset;

import java.util.List;

public final class TariffAdapter extends RecyclerView.Adapter<TariffViewHolder> {
    public static final String YOTA = "yota";
    public static final String MTS = "mts";
    public static final String BEELINE = "beeline";
    private List<TariffDataset> tariffs;
    private final EventListener eventListener;

    public TariffAdapter(EventListener eventListener, @NonNull List<TariffDataset> data) {
        this.tariffs = data;
        this.eventListener = eventListener;
    }

    @NonNull
    @Override
    public TariffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_tariffs_item, parent, false);
        return new TariffViewHolder(v, eventListener, tariffs);
    }

    @Override
    public void onBindViewHolder(@NonNull TariffViewHolder holder, int position) {
        holder.name.setText(tariffs.get(position).getName());
        holder.gigabyte.setText(tariffs.get(position).getGigabytes() + " ГБ");
        holder.sms.setText(tariffs.get(position).getSms() + " смс");
        holder.price.setText(tariffs.get(position).getPrice() + " р/мес");

        holder.icon.setContentDescription(this.tariffs.get(position).getOperator());
        holder.icon.setBackgroundColor(this.tariffs.get(position).getColor());
        holder.operator.setText(this.tariffs.get(position).getOperator());
    }

    @Override
    public int getItemCount() {
        return tariffs.size();
    }
}