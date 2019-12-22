package com.example.analyzer.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.analyzer.viewmodel.MainScreenViewModel;


public final class DetailsFragmentReusable extends Fragment {
    private static final String TYPE_OF_FRAGMENT = "TYPE";
    private MainScreenViewModel viewModel;

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
//        viewModel = new ViewModelProvider(getActivity()).get(MainScreenViewModel.class);
//        View view = inflater.inflate(R.layout.fragment_details_fragment_reusable, container, false);

//        final List<RecyclerDatasetReusable> reusableData = new ArrayList<>();
//        String operatorName = sp.getString("operatorName", getResources().getString(R.string.operator_not_rec));
//        if ("".equals(operatorName)) {
//            operatorName = getResources().getString(R.string.operator_not_rec);
//        }
//        final Bundle args = getArguments();
//
//        if (args != null) {
//            final String type = args.getString(TYPE_OF_FRAGMENT);
//            if (type != null && getActivity() != null) {
//                if (type.equals(getString(R.string.to_calls)) || type.equals(getString(R.string.to_detail))) {
//
//                    final SimpleDateFormat simpleDate =
//                            new SimpleDateFormat(getResources().getString(R.string.time_format), Locale.ENGLISH);
//                    final CallsRepository callsRepository = new CallsRepository(getActivity());
//                    final List<CallHistoryRecord> callHistoryRecords = callsRepository.getCalls();
//                    if (callHistoryRecords != null) {
//                        for (CallHistoryRecord record : callHistoryRecords) {
//                            reusableData.add(new RecyclerDatasetReusable(record.getName(), record.getAddress(),
//                                    simpleDate.format(record.getDate())));
//                        }
//                    }
//
//                    viewModel.getCalls().observe(getViewLifecycleOwner(), calls -> {
//                        if (calls != null) {
//                            viewModel.displayChart(barChart, getContext(), calls);
//                        }
//                    });
//                } else if (type.equals(getString(R.string.to_sms))) {
//                    final SimpleDateFormat simpleDate =
//                            new SimpleDateFormat(getResources().getString(R.string.time_format), Locale.ENGLISH);
//                    final SmsModule smsModule = new SmsModule(getActivity());
//                    final List<SmsHistoryRecord> smsHistoryRecords = smsModule.getSMS();
//                    if (smsHistoryRecords != null) {
//                        for (SmsHistoryRecord record : smsHistoryRecords) {
//                            reusableData.add(new RecyclerDatasetReusable(RecyclerAdapterReusable.unknown_number,
//                                    record.getAddress(), simpleDate.format(record.getDate())));
//                        }
//                    }
//                }
//            }
//        }

//        final RecyclerView recyclerView = view.findViewById(R.id.content_list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        final RecyclerAdapterReusable recyclerAdapter = new RecyclerAdapterReusable(reusableData);
//        recyclerView.setAdapter(recyclerAdapter);

//        return view;
        return null;
    }

}
