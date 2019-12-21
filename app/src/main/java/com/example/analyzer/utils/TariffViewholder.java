package com.example.analyzer.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;

class TariffViewholder extends RecyclerView.ViewHolder {
    final TextView name;
    final TextView gigabyte;
    final TextView sms;
    final TextView price;
    final ImageView icon;

    TariffViewholder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.tariffs_name);
        gigabyte = itemView.findViewById(R.id.tariffs_gigabytes);
        sms = itemView.findViewById(R.id.tariffs_sms);
        price = itemView.findViewById(R.id.tariffs_price);
        icon = itemView.findViewById(R.id.tariffs_operator_icon);
    }
}
