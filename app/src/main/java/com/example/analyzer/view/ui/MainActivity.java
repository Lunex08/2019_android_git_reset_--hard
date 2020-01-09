package com.example.analyzer.view.ui;

import android.Manifest;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.analyzer.R;
import com.example.analyzer.service.model.EventListener;
import com.example.analyzer.service.utils.PermissionsUtils;


public final class MainActivity extends AppCompatActivity implements EventListener {
    public final static String TAG = "MainActivityTag";
    PermissionsUtils permissionsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsUtils.getInstance().setActivity(this);
        PermissionsUtils.getInstance().checkAndRequestPermissions(Manifest.permission.READ_CALL_LOG);

        if (savedInstanceState == null) {
            final MainScreenFragment mainScreenFragment = new MainScreenFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, mainScreenFragment).commit();
        }
    }

    @Override
    public void onItemClick(int dest) {
//        final DetailsFragment detailsFragment;

        switch (dest) {
//            case R.string.to_detail:
//                detailsFragment = new DetailsFragment();
//                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, detailsFragment).addToBackStack(null).commit();
//                break;

            case R.string.to_tariffs:
                final TariffsFragment tariffsFragment = new TariffsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, tariffsFragment).addToBackStack(null).commit();
                break;
        }
    }
}
