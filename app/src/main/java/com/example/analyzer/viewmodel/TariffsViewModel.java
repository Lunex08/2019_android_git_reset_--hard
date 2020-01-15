package com.example.analyzer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.analyzer.service.model.Operator;
import com.example.analyzer.service.model.TariffDataset;
import com.example.analyzer.service.repository.AppDatabase;
import com.example.analyzer.service.repository.TariffRepository;

import java.util.List;

public class TariffsViewModel extends AndroidViewModel {
    private LiveData<List<TariffDataset>> mTariffs;

    private static AppDatabase appDatabase;

    public TariffsViewModel(@NonNull Application application) {
        super(application);
        mTariffs = TariffRepository.getInstance().getTariffs();
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(application.getApplicationContext(),
                    AppDatabase.class, "database").allowMainThreadQueries().build();
        }
        if (mTariffs.getValue().size() == 0) {
            MutableLiveData<List<TariffDataset>> operatorsObservable = new MutableLiveData<>();
            operatorsObservable.setValue(appDatabase.tariffsDao().getAll());
            mTariffs = operatorsObservable;
        } else {
            appDatabase.tariffsDao().clear();
            appDatabase.tariffsDao().insert(mTariffs.getValue());
        }
        refreshTariffs();
    }

    public LiveData<List<TariffDataset>> getTariffs() {
        return mTariffs;
    }

    public void refreshTariffs() {
        TariffRepository.getInstance().refreshTariffs();
    }
}
