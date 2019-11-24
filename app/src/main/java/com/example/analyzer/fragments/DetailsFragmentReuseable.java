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

import java.util.ArrayList;
import java.util.List;


public class DetailsFragmentReuseable extends Fragment {
    private List<Integer> numbers;
    final private static String TYPE_OF_FRAGMENT = "TYPE";

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

        numbers = new ArrayList<>();

        final Bundle args = getArguments();

        if (args != null) {
            final String type = args.getString(TYPE_OF_FRAGMENT);
            if (type != null) {
                if (type.equals(getString(R.string.to_calls)) || type.equals(getString(R.string.to_detail))) {
                    for (int i = 0; i < 10; ++i) {
                        numbers.add(i + 1);
                    }
                } else if (type.equals(getString(R.string.to_sms))) {
                    for (int i = 10; i > 0; --i) {
                        numbers.add(i);
                    }
                }
            }
        }

        final RecyclerView recyclerView = view.findViewById(R.id.content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final RecyclerAdapter recyclerAdapter = new RecyclerAdapter(numbers);
        recyclerView.setAdapter(recyclerAdapter);


        return view;
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {
        final private TextView singleValue;

        private RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            singleValue = itemView.findViewById(R.id.content_name);
        }
    }

    class RecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        final private List<Integer> values;

        private RecyclerAdapter(@NonNull List<Integer> values) {
            this.values = values;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_details_content_item, parent, false);
            return new RecyclerViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
            holder.singleValue.setText(String.valueOf(values.get(position)));
        }

        @Override
        public int getItemCount() {
            return values.size();
        }
    }
}
