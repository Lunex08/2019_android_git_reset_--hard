package com.example.analyzer.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.analyzer.R;

import java.util.EventListener;

public class DetailsFragment extends Fragment implements View.OnClickListener {
    private MainScreenFragment.EventListener eventListener;
    final public static String TO_CALLS = "TO_CALLS";
    final public static String TO_SMS = "TO_SMS";

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        eventListener = (MainScreenFragment.EventListener)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        final Button callsBtn = v.findViewById(R.id.calls_id);
        callsBtn.setOnClickListener(this);

        final Button smsBtn = v.findViewById(R.id.sms_id);
        smsBtn.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calls_id:
                eventListener.onItemClick(TO_CALLS);
                break;
            case R.id.sms_id:
                eventListener.onItemClick(TO_SMS);
                break;
        }
    }
}
