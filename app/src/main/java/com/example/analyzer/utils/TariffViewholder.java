package com.example.analyzer.utils;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;
import com.example.analyzer.fragments.MainScreenFragment;

class TariffViewholder extends RecyclerView.ViewHolder {
    final TextView name;
    final TextView gigabyte;
    final TextView sms;
    final TextView price;
    final ImageView icon;

    TariffViewholder(@NonNull View itemView, @NonNull MainScreenFragment.EventListener eventListener) {
        super(itemView);

        name = itemView.findViewById(R.id.tariffs_name);
        gigabyte = itemView.findViewById(R.id.tariffs_gigabytes);
        sms = itemView.findViewById(R.id.tariffs_sms);
        price = itemView.findViewById(R.id.tariffs_price);
        icon = itemView.findViewById(R.id.tariffs_operator_icon);

        itemView.setOnClickListener(v -> {
            final TextView name_view = v.findViewById(R.id.tariffs_name);
            final TextView gigabyte_view = v.findViewById(R.id.tariffs_gigabytes);
            final TextView sms_view = v.findViewById(R.id.tariffs_sms);
            final TextView price_view = v.findViewById(R.id.tariffs_price);
            final ImageView icon_view = v.findViewById(R.id.tariffs_operator_icon);
            final String name = name_view.getText().toString();
            final String gigabyte = gigabyte_view.getText().toString();
            final String sms = sms_view.getText().toString();
            final String price = price_view.getText().toString();
            final String icon = icon_view.getContentDescription().toString();

            eventListener.onTariffClick(name, gigabyte, sms, price, icon);
        });
    }
}
