package com.example.analyzer;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.analyzer.fragments.MainScreenFragment;
import com.example.analyzer.modules.CallsModule;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = "MainActivityTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // example how to get calls history
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy hh/mm/ss");
        CallsModule callsModule = new CallsModule(this.getApplicationContext());
        for (CallsModule.CallHistoryRecord record : callsModule.getCalls()) {
            Log.d(TAG, "\nPhone Number:--- " + record.getPhNumber() + " \nCall Type:--- " + record.getType() +
                    "\nCall duration in sec:--- " + record.getDuration() + "\nCall name:--- " + record.getName() +
                    "\nCall date:--- " + simpleDate.format(record.getDate()) + "\n----------------------------------");
        }

        if (savedInstanceState == null) {
            MainScreenFragment mainScreenFragment = MainScreenFragment.getInstance();

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_activity_container, mainScreenFragment);
            ft.commit();
        }
    }
}
