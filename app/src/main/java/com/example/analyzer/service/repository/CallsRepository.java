package com.example.analyzer.service.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.analyzer.service.model.CallHistoryRecord;
import com.example.analyzer.service.model.CallsService;

import java.util.List;

public class CallsRepository {
    private static final CallsRepository mInstance = new CallsRepository();
    private static MutableLiveData<List<CallHistoryRecord>> data;

    public static CallsRepository getInstance() {
        return mInstance;
    }

    public CallsRepository() {
        data = new MutableLiveData<>();
    }

    public LiveData<List<CallHistoryRecord>> getCalls() {
        return data;
    }

    public void loadCalls(@NonNull Context context) {
        data.setValue(CallsService.getCalls(context));
    }
}
