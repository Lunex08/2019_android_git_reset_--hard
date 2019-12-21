package com.example.analyzer.fragments;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;


public class DetailsFragmentReusable extends Fragment {
    private static final String TYPE_OF_FRAGMENT = "TYPE";

    private List<Integer> numbers;

    public static DetailsFragmentReusable newInstance(String type) {
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

        numbers = new ArrayList<>();

        final Bundle args = getArguments();

        if (args != null) {
            final String type = args.getString(TYPE_OF_FRAGMENT);
            if (type != null) {
                if (type.equals(getString(R.string.to_calls)) || type.equals(getString(R.string.to_detail))) {
                    numbers.add(1);
                } else if (type.equals(getString(R.string.to_sms))) {
                    numbers.add(2);
                }
            }
        }

        final RecyclerView recyclerView = view.findViewById(R.id.content_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        final RecyclerAdapterReusable recyclerAdapter = new RecyclerAdapterReusable(numbers);
        recyclerView.setAdapter(recyclerAdapter);


        return view;
    }

}
