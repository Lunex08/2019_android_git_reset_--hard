package com.example.analyzer.service.model;

import androidx.annotation.NonNull;

import java.util.List;

public interface EventListener {
    void showDetailsFragment(List<CallHistoryRecord> calls, List<SmsHistoryRecord> sms);

    void showTariffsFragment();

    void showMainFragment();

    void showInfoFragment();

    void showTariffDifferenceFragment(@NonNull String name,
                                      @NonNull String gigabyte,
                                      @NonNull String sms,
                                      @NonNull String price,
                                      @NonNull String icon,
                                      @NonNull int color,
                                      @NonNull List<TariffDataset> tariffs);
}