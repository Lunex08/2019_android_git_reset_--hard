package com.example.analyzer.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Room;

import com.example.analyzer.service.model.Operator;
import com.example.analyzer.service.model.TariffDataset;
import com.example.analyzer.service.repository.AppDatabase;
import com.example.analyzer.service.repository.TariffRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InfoFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<List<TariffDataset>> mTariffs;
    private MutableLiveData<List<Operator>> mOperators;
    private static AppDatabase appDatabase;
    private static final String MY_SETTINGS = "my_settings";

    public InfoFragmentViewModel(@NonNull Application application) {
        super(application);
        mOperators = TariffRepository.getInstance().getOperators();
        mTariffs = TariffRepository.getInstance().getTariffs();
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(application.getApplicationContext(),
                    AppDatabase.class, "database").allowMainThreadQueries().build();
        }
        if (mOperators.getValue().size() == 0) {
            List<Operator> operators = appDatabase.operatorsDao().getAll();
            if (operators != null && operators.size() > 0) {
                mOperators.setValue(operators);
            } else {
                refreshOperators();
            }
        } else {
            appDatabase.operatorsDao().clear();
            appDatabase.operatorsDao().insert(mOperators.getValue());
        }
        if (mTariffs.getValue().size() == 0) {
            List<TariffDataset> tariffs = appDatabase.tariffsDao().getAll();
            if (tariffs != null && tariffs.size() > 0) {
                mTariffs.setValue(tariffs);
            } else {
                refreshOperators();
            }
        } else {
            appDatabase.tariffsDao().clear();
            appDatabase.tariffsDao().insert(mTariffs.getValue());
        }
        refreshTariffs();
        refreshOperators();
    }

    public List<String> getTariffs(@NonNull String operatorName, @NonNull SharedPreferences sp1) {
        final List<String> tariffNames = new ArrayList<>();
        tariffNames.add("Тариф");
        if (mTariffs != null) {
            for (TariffDataset tariff : Objects.requireNonNull(mTariffs.getValue())) {
                if (tariff.getOperator().toLowerCase().equals(operatorName.toLowerCase())) {
                    tariffNames.add(tariff.getName());
                }
            }
        } else {
            mTariffs = TariffRepository.getInstance().getTariffs();
        }
        for (Operator tmp: mOperators.getValue()) {
            if (operatorName.equals(tmp.getOperator())) {
                SharedPreferences.Editor e = sp1.edit();
                e.putString("color", tmp.getColor());
                e.apply();
            }
        }
        return tariffNames;
    }

    public void refreshTariffs() {
        TariffRepository.getInstance().refreshTariffs();
    }

    public LiveData<List<String>> getOperators() {
        if (mOperators == null) {
            mOperators = TariffRepository.getInstance().getOperators();
            appDatabase.operatorsDao().clear();
            appDatabase.operatorsDao().insert(mOperators.getValue());
        }
        return Transformations.map(mOperators, this::transformOperatorsToNamesList);
    }

    private List<String> transformOperatorsToNamesList(List<Operator> operators) {
        final List<String> operatorNames = new ArrayList<>();
        operatorNames.add("Оператор");
        for (Operator operator : operators) {
            operatorNames.add(operator.getOperator());
        }
        return operatorNames;
    }

    public void refreshOperators() {
        TariffRepository.getInstance().refreshOperators();
    }
}
