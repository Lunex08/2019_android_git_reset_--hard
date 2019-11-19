package com.example.analyzer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        Toolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle("MyAnalyzer");
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.settings_menu);
//        toolbar.setOnMenuItemClickListener(a->{ return true;});
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        final CardView cardView = v.findViewById(R.id.card_view);
        cardView.setOnClickListener(this);

        return v;
    }
  
    public void onClick(View v) {
        // View your tariff fragment
    }
}
