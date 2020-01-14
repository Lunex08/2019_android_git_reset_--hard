package com.example.analyzer.view.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.analyzer.R;
import com.example.analyzer.service.model.EventListener;
import com.example.analyzer.viewmodel.InfoFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {
    private EventListener eventListener;
    private EditText phoneEdit;
    private Spinner operatorSpinner;
    private Spinner tariffSpiner;
    private static final String MY_SETTINGS = "my_settings";
    private List<String> operatorsArray = new ArrayList<String>();
    private List<String> tariffsArray = new ArrayList<String>();
    private String operatorName = "";
    private String tariffName = "";
    private InfoFragmentViewModel viewModel;

    @Override
    public void onDetach() {
        super.onDetach();
        eventListener = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        eventListener = (EventListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        viewModel = new ViewModelProvider(this).get(InfoFragmentViewModel.class);

        phoneEdit = v.findViewById(R.id.phone_number);
        operatorSpinner = v.findViewById(R.id.operator_select);
        tariffSpiner = v.findViewById(R.id.tariff_select);
        SharedPreferences sp = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        boolean firstLogin = sp.getBoolean("firstLogin", true);

        if (!firstLogin) {
            String phoneNumber = sp.getString("phoneNumber", "");
            phoneEdit.setText(phoneNumber);
            operatorName = sp.getString("operatorName", "");
            tariffName = sp.getString("tariffName", "");
        }

        final Button callsBtn = v.findViewById(R.id.info_save);
        callsBtn.setOnClickListener(v1 -> {
            String phoneNumber = phoneEdit.getText().toString();
            SharedPreferences sp1 = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
            SharedPreferences.Editor e = sp1.edit();

            e.putString("phoneNumber", phoneNumber);
            e.putString("operatorName", operatorName);
            e.putString("tariffName", tariffName);
            e.putBoolean("firstLogin", false);
            e.apply();

            eventListener.showMainFragment();
        });

        ArrayAdapter<String> tariffsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                tariffsArray);
        tariffsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tariffSpiner.setAdapter(tariffsAdapter);
        tariffSpiner.setEnabled(false);
        tariffSpiner.setSelection(0);
        tariffSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int myPosition, long myID) {
                tariffName = (myID != 0) ? tariffSpiner.getSelectedItem().toString() : "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        tariffsArray.add("Тариф");
        operatorsArray.add("Оператор");
        ArrayAdapter<String> operatorsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item
                , operatorsArray);
        operatorsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSpinner.setAdapter(operatorsAdapter);
        operatorSpinner.setSelection(0);
        operatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int myPosition, long myID) {
                tariffSpiner.setEnabled(false);
                if (myID != 0) {
                    operatorName = operatorSpinner.getSelectedItem().toString();
                    tariffSpiner.setEnabled(true);
                } else {
                    operatorName = "";
                }
                tariffsArray.clear();
                tariffsArray.addAll(viewModel.getTariffs(operatorName));
                tariffsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        viewModel.getOperators().observe(getViewLifecycleOwner(), updatedOperators -> {
            operatorsArray.clear();
            operatorsArray.addAll(updatedOperators);
            operatorsAdapter.notifyDataSetChanged();
        });
        viewModel.refreshOperators();
        viewModel.refreshTariffs();

        return v;
    }
}