package com.example.analyzer.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.example.analyzer.service.model.Operator;
import com.example.analyzer.service.model.TariffDataset;
import com.example.analyzer.service.repository.TariffRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InfoFragmentViewModel extends AndroidViewModel {
    private final LiveData<List<TariffDataset>> mTariffs;
    private final LiveData<List<Operator>> mOperators;

    public InfoFragmentViewModel(@NonNull Application application) {
        super(application);
        mTariffs = TariffRepository.getInstance().getTariffs();
        mOperators = TariffRepository.getInstance().getOperators();
        refreshTariffs();
    }

    public List<String> getTariffs(String operatorName) {
        final List<String> tariffNames = new ArrayList<>();
        if (mTariffs != null) {
            for (TariffDataset tariff : Objects.requireNonNull(mTariffs.getValue())) {
                if (tariff.getOperator().toLowerCase().equals(operatorName.toLowerCase())) {
                    tariffNames.add(tariff.getName());
                }
            }
        }
        return tariffNames;
    }

    public void refreshTariffs() {
        TariffRepository.getInstance().refreshTariffs();
    }

    public LiveData<List<String>> getOperators() {
        return Transformations.map(mOperators, this::transformOperatorsToNamesList);
    }

    private List<String> transformOperatorsToNamesList(List<Operator> operators) {
        final List<String> operatorNames = new ArrayList<>();
        for (Operator operator : operators) {
            operatorNames.add(operator.getOperator());
        }
        return operatorNames;
    }

    public void refreshOperators() {
        TariffRepository.getInstance().refreshOperators();
    }
}
