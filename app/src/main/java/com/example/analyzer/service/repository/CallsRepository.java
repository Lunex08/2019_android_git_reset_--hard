package com.example.analyzer.service.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.analyzer.service.model.CallHistoryRecord;
import com.example.analyzer.service.model.CallsService;

import java.util.List;

public final class CallsRepository {
    private final Context cxt;
    private MutableLiveData<List<CallHistoryRecord>> data;

    public CallsRepository(@NonNull Context cxt) {
        this.cxt = cxt;
        data = new MutableLiveData<>();
    }

    public LiveData<List<CallHistoryRecord>> getCalls() {
        data.setValue(CallsService.getCalls(cxt));
        return data;
    }

}
