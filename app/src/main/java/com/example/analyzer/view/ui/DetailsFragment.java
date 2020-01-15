package com.example.analyzer.view.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.analyzer.R;
import com.example.analyzer.service.model.CallHistoryRecord;
import com.example.analyzer.service.model.EventListener;
import com.example.analyzer.service.model.SmsHistoryRecord;

import java.util.List;

public final class DetailsFragment extends Fragment implements View.OnClickListener {
    private EventListener eventListener;
    private List<CallHistoryRecord> calls;
    public static final String MY_SETTINGS = "my_settings";
    private List<SmsHistoryRecord> sms;

    public DetailsFragment(List<CallHistoryRecord> calls, List<SmsHistoryRecord> sms) {
        this.calls = calls;
        this.sms = sms;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        eventListener = (EventListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        eventListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);

        final Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.details);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);
        SharedPreferences sp1 = getActivity().getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        toolbar.setBackgroundColor(Color.parseColor(sp1.getString("color", "#008577")));

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            eventListener.showInfoFragment();
            return true;
        });

        final Button callsBtn = v.findViewById(R.id.calls_id);
        callsBtn.setOnClickListener(this);

        final Button smsBtn = v.findViewById(R.id.sms_id);
        smsBtn.setOnClickListener(this);

        final DetailsFragmentReusable detailsFragmentReusable =
         new DetailsFragmentReusable(getString(R.string.to_calls), calls, null);

        getChildFragmentManager().beginTransaction().replace(R.id.fragment_details_content, detailsFragmentReusable).addToBackStack(null).commit();

        return v;
    }

    @Override
    public void onClick(View v) {
        final DetailsFragmentReusable detailsFragmentReusable;

        switch (v.getId()) {
            default:
            case R.id.calls_id:
                detailsFragmentReusable = new DetailsFragmentReusable(getString(R.string.to_calls), calls, null);
                break;
            case R.id.sms_id:
                detailsFragmentReusable = new DetailsFragmentReusable(getString(R.string.to_sms), null, sms);
                break;
        }

        getChildFragmentManager().beginTransaction().replace(R.id.fragment_details_content, detailsFragmentReusable).addToBackStack(null).commit();
    }
}