package com.example.analyzer.service.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.analyzer.service.model.SmsHistoryRecord;
import com.example.analyzer.service.model.SmsService;

import java.util.List;

public class SmsRepository {
    private static final SmsRepository mInstance = new SmsRepository();
    private static MutableLiveData<List<SmsHistoryRecord>> data;

    public static SmsRepository getInstance() {
        return mInstance;
    }

    public SmsRepository() {
        data = new MutableLiveData<>();
    }

    public LiveData<List<SmsHistoryRecord>> getSms() {
        return data;
    }

    public void loadSms(@NonNull Context context) {
        data.setValue(SmsService.getSms(context));
    }
}
