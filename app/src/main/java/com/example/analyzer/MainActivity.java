package com.example.analyzer;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.analyzer.fragments.DetailsFragment;
import com.example.analyzer.fragments.DetailsFragmentReuseable;
import com.example.analyzer.fragments.MainScreenFragment;
import com.example.analyzer.fragments.TariffsFragment;
import com.example.analyzer.utils.PermissionsUtils;


public class MainActivity extends AppCompatActivity implements MainScreenFragment.EventListener {
//    public final static String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsUtils.checkAndRequestPermissions(this);

        if (savedInstanceState == null) {
            final MainScreenFragment mainScreenFragment = MainScreenFragment.getInstance();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_container, mainScreenFragment)
                    .commit();
        }
    }

    @Override
    public void onItemClick(int dest) {
        final DetailsFragment detailsFragment;
        final DetailsFragmentReuseable detailsFragmentReuseable;

        switch (dest) {
            case R.string.to_detail:
                detailsFragment = new DetailsFragment();
                detailsFragmentReuseable = DetailsFragmentReuseable.newInstance(getString(dest));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_container, detailsFragment)
                        .replace(R.id.fragment_details_content, detailsFragmentReuseable)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.string.to_calls:
            case R.string.to_sms:
                detailsFragmentReuseable = DetailsFragmentReuseable.newInstance(getString(dest));

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_details_content, detailsFragmentReuseable)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.string.to_tariffs:
                final TariffsFragment tariffsFragment = new TariffsFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_container, tariffsFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
}
