package com.example.analyzer.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.analyzer.R;
import com.example.analyzer.service.model.SmsHistoryRecord;
import com.example.analyzer.service.model.SmsModule;
import com.example.analyzer.service.utils.RecyclerAdapterReusable;
import com.example.analyzer.service.utils.RecyclerDatasetReusable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public final class DetailsFragmentReusable extends Fragment {
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

                    final SimpleDateFormat simpleDate =
                            new SimpleDateFormat(getResources().getString(R.string.time_format), Locale.ENGLISH);
                    //                    final CallsRepository callsRepository = new CallsRepository(getActivity());
                    //                    final List<CallHistoryRecord> callHistoryRecords = callsRepository.getCalls();
                    //                    if(callHistoryRecords != null) {
                    //                        for (CallHistoryRecord record : callHistoryRecords) {
                    //                            reusableData.add(new RecyclerDatasetReusable(record.getName(),
                    //                            record.getAddress(), simpleDate.format(record.getDate())));
                    //                        }
                    //                    }
                } else if (type.equals(getString(R.string.to_sms))) {
                    final SimpleDateFormat simpleDate =
                            new SimpleDateFormat(getResources().getString(R.string.time_format), Locale.ENGLISH);
                    final SmsModule smsModule = new SmsModule(getActivity());
                    final List<SmsHistoryRecord> smsHistoryRecords = smsModule.getSMS();
                    if (smsHistoryRecords != null) {
                        for (SmsHistoryRecord record : smsHistoryRecords) {
                            reusableData.add(new RecyclerDatasetReusable(RecyclerAdapterReusable.unknown_number,
                                    record.getAddress(), simpleDate.format(record.getDate())));
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
