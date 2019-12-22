package com.example.analyzer.view.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

import com.example.analyzer.R;

public final class DetailsFragment extends Fragment implements View.OnClickListener {
    private MainScreenFragment.EventListener eventListener;

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

        final Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.details);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        final Button callsBtn = v.findViewById(R.id.calls_id);
        callsBtn.setOnClickListener(this);

        final Button smsBtn = v.findViewById(R.id.sms_id);
        smsBtn.setOnClickListener(this);

        final DetailsFragmentReusable detailsFragmentReusable = DetailsFragmentReusable.newInstance(getString(R.string.to_calls));

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_details_content, detailsFragmentReusable)
                .addToBackStack(null)
                .commit();

        return v;
    }

    @Override
    public void onClick(View v) {
        final DetailsFragmentReusable detailsFragmentReusable;

        switch (v.getId()) {
            default:
            case R.id.calls_id:
                detailsFragmentReusable = DetailsFragmentReusable.newInstance(getString(R.string.to_calls));
                break;
            case R.id.sms_id:
                detailsFragmentReusable = DetailsFragmentReusable.newInstance(getString(R.string.to_sms));
                break;
        }

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_details_content, detailsFragmentReusable)
                .addToBackStack(null)
                .commit();
    }
}
