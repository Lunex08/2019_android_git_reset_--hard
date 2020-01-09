package com.example.analyzer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.analyzer.service.model.TariffDataset;
import com.example.analyzer.service.repository.TariffRepository;

import java.util.List;

public class TariffsViewModel extends AndroidViewModel {
    private final LiveData<List<TariffDataset>> mTariffs;

    public TariffsViewModel(@NonNull Application application) {
        super(application);
        mTariffs = TariffRepository.getInstance().getTariffs();
        refreshTariffs();
    }

    public LiveData<List<TariffDataset>> getTariffs() {
        return mTariffs;
    }

    public void refreshTariffs() {
        TariffRepository.getInstance().refreshTariffs();
    }
}
