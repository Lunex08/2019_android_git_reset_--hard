package com.example.analyzer.fragments;

import android.os.Bundle;

import com.example.analyzer.modules.DataModule.CallHistoryRecord;
import com.example.analyzer.modules.DataModule.CallsModule;
import com.example.analyzer.modules.DataModule.SmsHistoryRecord;
import com.example.analyzer.modules.DataModule.SmsModule;
import com.example.analyzer.utils.RecyclerAdapterReusable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.analyzer.R;
import com.example.analyzer.utils.RecyclerDatasetReusable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class DetailsFragmentReusable extends Fragment {
    private static final String TYPE_OF_FRAGMENT = "TYPE";

    static DetailsFragmentReusable newInstance(String type) {
        final Bundle args = new Bundle();
        args.putString(TYPE_OF_FRAGMENT, type);
        
        final DetailsFragmentReusable fragment = new DetailsFragmentReusable();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_fragment_reusable, container, false);

        final List<RecyclerDatasetReusable> reusableData = new ArrayList<>();

        final Bundle args = getArguments();

        if (args != null) {
            final String type = args.getString(TYPE_OF_FRAGMENT);
            if (type != null && getActivity() != null) {
                if (type.equals(getString(R.string.to_calls)) || type.equals(getString(R.string.to_detail))) {

                    final SimpleDateFormat simpleDate = new SimpleDateFormat(getResources().getString(R.string.time_format), Locale.ENGLISH);
                    final CallsModule callsModule = new CallsModule(getActivity());
                    final List<CallHistoryRecord> callHistoryRecords = callsModule.getCalls();
                    if(callHistoryRecords != null) {
                        for (CallHistoryRecord record : callHistoryRecords) {
                            reusableData.add(new RecyclerDatasetReusable(record.getName(), record.getAddress(), simpleDate.format(record.getDate())));
                        }
                    }
                } else if (type.equals(getString(R.string.to_sms))) {
                    final SimpleDateFormat simpleDate = new SimpleDateFormat(getResources().getString(R.string.time_format), Locale.ENGLISH);
                    final SmsModule smsModule = new SmsModule(getActivity());
                    final List<SmsHistoryRecord> smsHistoryRecords = smsModule.getSMS();
                    if(smsHistoryRecords != null) {
                        for (SmsHistoryRecord record : smsHistoryRecords) {
                           reusableData.add(new RecyclerDatasetReusable(RecyclerAdapterReusable.unknown_number, record.getAddress(), simpleDate.format(record.getDate())));
                        }
                    }
                }
            }
        }

        final RecyclerView recyclerView = view.findViewById(R.id.content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final RecyclerAdapterReusable recyclerAdapter = new RecyclerAdapterReusable(reusableData);
        recyclerView.setAdapter(recyclerAdapter);

        return view;
    }

}
