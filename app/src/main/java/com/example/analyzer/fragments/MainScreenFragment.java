package com.example.analyzer.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.analyzer.R;

public class MainScreenFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "MainScreenFragmentTag";

    public static MainScreenFragment getInstance() {
        return new MainScreenFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.main_screen_fragment, container, false);

        final Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle("Analyzer");
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);

        final Menu menu = toolbar.getMenu();
        menu.findItem(R.id.settings_menu).setOnMenuItemClickListener((menuItem) -> {
            Log.d(TAG, "Toolbar menu clicked");
            return true;
        });


        final CardView cardView = v.findViewById(R.id.card_view);
        cardView.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {
        Log.d(TAG, "Tarif cardView clicked");
    }
}
