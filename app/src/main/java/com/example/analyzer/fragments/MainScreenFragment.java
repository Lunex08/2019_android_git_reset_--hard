package com.example.analyzer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.analyzer.R;

public class MainScreenFragment extends Fragment implements View.OnClickListener {
    public static MainScreenFragment getInstance() {
        return new MainScreenFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.main_screen_fragment, container, false);

        final CardView cardView = v.findViewById(R.id.card_view);
        cardView.setOnClickListener(this);

        final Button tariffsButton = (Button) v.findViewById(R.id.title_tariffs_button);
        tariffsButton.setOnClickListener(this);

        final Button eventsButton = (Button) v.findViewById(R.id.title_events_button);
        eventsButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        // View your tariff fragment
    }
}
