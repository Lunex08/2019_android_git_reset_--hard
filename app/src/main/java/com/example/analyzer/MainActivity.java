package com.example.analyzer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.analyzer.fragments.DetailsFragment;
import com.example.analyzer.fragments.DetailsFragmentReuseable;
import com.example.analyzer.fragments.MainScreenFragment;

public class MainActivity extends AppCompatActivity implements MainScreenFragment.EventListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            MainScreenFragment mainScreenFragment = MainScreenFragment.getInstance();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_activity_container, mainScreenFragment);
            ft.commit();
        }
    }

    @Override
    public void onItemClick(String dest) {
        final DetailsFragment detailsFragment;
        final DetailsFragmentReuseable detailsFragmentReuseable;

        switch (dest) {
            case MainScreenFragment.TO_DETAL:
                detailsFragment = new DetailsFragment();
                detailsFragmentReuseable = DetailsFragmentReuseable.newInstance(dest);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_container, detailsFragment)
                        .replace(R.id.fragment_details_content, detailsFragmentReuseable)
                        .addToBackStack(null)
                        .commit();
                break;
            case DetailsFragment.TO_CALLS:
            case DetailsFragment.TO_SMS:
                detailsFragmentReuseable = DetailsFragmentReuseable.newInstance(dest);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_details_content, detailsFragmentReuseable)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
