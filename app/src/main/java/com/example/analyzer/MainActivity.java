package com.example.analyzer;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.analyzer.fragments.DetailsFragment;
import com.example.analyzer.fragments.InfoFragment;
import com.example.analyzer.fragments.MainScreenFragment;
import com.example.analyzer.fragments.TariffsFragment;
import com.example.analyzer.modules.DataModule.CallHistoryRecord;
import com.example.analyzer.modules.DataModule.CallsModule;
import com.example.analyzer.utils.PermissionsUtils;

import java.text.SimpleDateFormat;
import java.util.List;


public final class MainActivity extends AppCompatActivity implements MainScreenFragment.EventListener {
    public final static String TAG = "MainActivityTag";
    private static final String MY_SETTINGS = "my_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsUtils.checkAndRequestPermissions(this, Manifest.permission.READ_CALL_LOG);

        // example how to get calls history
        final SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy ss/mm/hh");
        final CallsModule callsModule = new CallsModule(this);
        final List<CallHistoryRecord> callHistoryRecords = callsModule.getCalls();
        if(callHistoryRecords != null) {
            for (CallHistoryRecord record : callHistoryRecords) {
                Log.d(TAG, "\nPhone Number:--- " + record.getAddress() + " \nCall Type:--- " + record.getStatus() +
                        "\nCall duration in sec:--- " + record.getDuration() + "\nCall name:--- " + record.getName() +
                        "\nCall date:--- " + simpleDate.format(record.getDate()) + "\n----------------------------------");
            }
        }

        SharedPreferences sp = getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        boolean firstLogin = sp.getBoolean("firstLogin", true);
//        if (true) {
         if (firstLogin) {
            final InfoFragment infoFragment = InfoFragment.getInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_container, infoFragment)
                    .commit();
            return;
        }

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

        switch (dest) {
            case R.string.to_detail:
                detailsFragment = new DetailsFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_activity_container, detailsFragment)
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
