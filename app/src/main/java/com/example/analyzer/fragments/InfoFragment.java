package com.example.analyzer.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.analyzer.R;
import com.example.analyzer.utils.NetworkService;
import com.example.analyzer.utils.Operator;
import com.example.analyzer.utils.Post;
import com.example.analyzer.utils.TariffDataset;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoFragment extends Fragment {
    public static InfoFragment getInstance() {
        return new InfoFragment();
    }
    private MainScreenFragment.EventListener eventListener;
    private EditText phoneEdit;
    private Spinner operatorSpinner;
    private Spinner tariffSpiner;
    private static final String MY_SETTINGS = "my_settings";
    private List<String> operatorsArray =  new ArrayList<String>();
    private List<String> tariffsArray =  new ArrayList<String>();
    private String operatorName = "";
    private String tariffName = "";

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        eventListener = (MainScreenFragment.EventListener)context;
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        eventListener = null;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        phoneEdit = v.findViewById(R.id.phone_number);
        operatorSpinner = v.findViewById(R.id.operator_select);
        tariffSpiner = v.findViewById(R.id.tariff_select);
        final Button callsBtn = v.findViewById(R.id.info_save);
        callsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneEdit.getText().toString();
                SharedPreferences sp = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
                SharedPreferences.Editor e = sp.edit();
                e.putString("phoneNumber", phoneNumber);
                e.putString("operatorName", operatorName);
                e.putBoolean("firstLogin", false);
                e.commit(); // не забудьте подтвердить изменения
                final MainScreenFragment mainScreenFragment = MainScreenFragment.getInstance();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_container, mainScreenFragment)
                        .commit();
            }
        });

        operatorsArray.add("Оператор");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, operatorsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operatorSpinner.setAdapter(adapter);
        operatorSpinner.setSelection(0);
        operatorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int myPosition, long myID) {
                operatorName = (myID != 0) ? operatorSpinner.getSelectedItem().toString() : "";
                NetworkService.getInstance()
                        .getJSONApi()
                        .getAllPosts()
                        .enqueue(new Callback<List<Post>>() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                                tariffsArray.clear();
                                tariffsArray.add("Тариф");
                                List<Post> posts = response.body();
                                if (posts != null) {
                                    for (Post post : posts) {
                                        if (post.getOperator().toLowerCase().equals(operatorName.toLowerCase())) {
                                            tariffsArray.add(String.format("%s %.2f₽", post.getName(), post.getPrice()));
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        tariffsArray.add("Тариф");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, tariffsArray);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tariffSpiner.setAdapter(adapter2);
        tariffSpiner.setSelection(0);
        tariffSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int myPosition, long myID) {
                tariffName = (myID != 0) ? tariffSpiner.getSelectedItem().toString() : "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        NetworkService.getInstance()
                .getJSONApi()
                .getAllOperators()
                .enqueue(new Callback<List<Operator>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Operator>> call, @NonNull Response<List<Operator>> response) {
                        List<Operator> operators = response.body();
                        if (operators == null) return;
                        for (Operator operator : operators) {
                            operatorsArray.add(operator.getOperator());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Operator>> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });

        SharedPreferences sp = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        boolean firstLogin = sp.getBoolean("firstLogin", true);
        if (!firstLogin) {
            String phoneNumber = sp.getString("phoneNumber", ""); // Прочесть номер
            phoneEdit.setText(phoneNumber);
            operatorName = sp.getString("operatorName", ""); // Прочесть оператора
            tariffName = sp.getString("tariffName", ""); // Прочесть тариф
        }

        return v;
    }

//    @Override
//    public void onClick(View v) {
////        switch (v.getId()) {
////            case R.id.calls_id:
////                eventListener.onItemClick(R.string.to_calls);
////                break;
////            case R.id.sms_id:
////                eventListener.onItemClick(R.string.to_sms);
////                break;
////        }
//    }
}