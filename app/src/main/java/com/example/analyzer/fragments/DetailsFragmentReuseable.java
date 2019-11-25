package com.example.analyzer.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.analyzer.R;
import com.example.analyzer.modules.CallsModule.CallHistoryRecord;
import com.example.analyzer.modules.CallsModule.CallsModule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class DetailsFragmentReuseable extends Fragment {
    private static final String TYPE_OF_FRAGMENT = "TYPE";

//    private List<String> reusableNames;
//    private List<String> reusablePhones;
//    private List<String> reusableDates;

    public static DetailsFragmentReuseable newInstance(String type) {
        final Bundle args = new Bundle();
        args.putString(TYPE_OF_FRAGMENT, type);

        final DetailsFragmentReuseable fragment = new DetailsFragmentReuseable();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_fragment_reuseable, container, false);

        List<String> reusableNames = new ArrayList<>();
        List<String> reusablePhones = new ArrayList<>();
        List<String> reusableDates = new ArrayList<>();

        final Bundle args = getArguments();

        if (args != null) {
            final String type = args.getString(TYPE_OF_FRAGMENT);
            if (type != null && getActivity() != null) {
                if (type.equals(getString(R.string.to_calls)) || type.equals(getString(R.string.to_detail))) {

                    SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy ss/mm/hh");
                    CallsModule callsModule = new CallsModule(getActivity());
                    List<CallHistoryRecord> callHistoryRecords = callsModule.getCalls();
                    if(callHistoryRecords != null) {
                        for (CallHistoryRecord record : callHistoryRecords) {
                            reusableNames.add(record.getName());
                            reusablePhones.add(record.getPhNumber());
                            reusableDates.add(simpleDate.format(record.getDate()));
                        }
                    }
                } else if (type.equals(getString(R.string.to_sms))) {
                    SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy ss/mm/hh");
                    CallsModule callsModule = new CallsModule(getActivity());
                    List<CallHistoryRecord> callHistoryRecords = callsModule.getCalls();
                    if(callHistoryRecords != null) {
                        for (CallHistoryRecord record : callHistoryRecords) {
                            reusableNames.add(record.getName());
                            reusablePhones.add(record.getPhNumber());
                            reusableDates.add(simpleDate.format(record.getDate()));
                        }
                    }
                }
            }
        }

        final RecyclerView recyclerView = view.findViewById(R.id.content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(reusableNames, reusablePhones, reusableDates);
        recyclerView.setAdapter(recyclerAdapter);


        return view;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView phone;
        private final TextView date;

        private RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.content_name);
            phone = itemView.findViewById(R.id.content_phone);
            date = itemView.findViewById(R.id.content_date);
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private final List<String> names;
        private final List<String> phones;
        private final List<String> dates;

        private RecyclerAdapter(@NonNull List<String> names, @NonNull List<String> phones, @NonNull List<String> dates) {
            this.names = names;
            this.phones = phones;
            this.dates = dates;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_details_content_item, parent, false);
            return new RecyclerViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            if (names.get(position) == null) {
                holder.name.setText(getResources().getString(R.string.unknown_number));
            } else {
                holder.name.setText(String.valueOf(names.get(position)));
            }

            holder.phone.setText(String.valueOf(phones.get(position)));
            holder.date.setText(String.valueOf(dates.get(position)));
        }

        @Override
        public int getItemCount() {
            return names.size();
        }
    }
}
