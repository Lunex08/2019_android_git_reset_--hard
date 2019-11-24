package com.example.analyzer;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.analyzer.fragments.DetailsFragment;
import com.example.analyzer.fragments.DetailsFragmentReuseable;
import com.example.analyzer.fragments.MainScreenFragment;
import com.example.analyzer.modules.CallsModule.CallHistoryRecord;
import com.example.analyzer.modules.CallsModule.CallsModule;
import com.example.analyzer.utils.PermissionsUtils;

import java.text.SimpleDateFormat;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MainScreenFragment.EventListener {
    public final static String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionsUtils.checkAndRequestPermissions(this);

        // example how to get calls history
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy ss/mm/hh");
        CallsModule callsModule = new CallsModule(this);
        List<CallHistoryRecord> callHistoryRecords = callsModule.getCalls();
        if(callHistoryRecords != null) {
            for (CallHistoryRecord record : callHistoryRecords) {
                Log.d(TAG, "\nPhone Number:--- " + record.getPhNumber() + " \nCall Type:--- " + record.getType() +
                        "\nCall duration in sec:--- " + record.getDuration() + "\nCall name:--- " + record.getName() +
                        "\nCall date:--- " + simpleDate.format(record.getDate()) + "\n----------------------------------");
            }
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
        }
    }
}
