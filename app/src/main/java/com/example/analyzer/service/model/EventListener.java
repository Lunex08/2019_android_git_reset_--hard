package com.example.analyzer.service.model;

import java.util.List;

public interface EventListener {
    void showDetailsFragment(List<CallHistoryRecord> calls, List<SmsHistoryRecord> sms);
    void showTariffsFragment();
    void showMainFragment();
    void showInfoFragment();
}