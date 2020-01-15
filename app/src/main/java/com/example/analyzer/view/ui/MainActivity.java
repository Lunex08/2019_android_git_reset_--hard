package com.example.analyzer.view.ui;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.analyzer.R;
import com.example.analyzer.service.model.CallHistoryRecord;
import com.example.analyzer.service.model.EventListener;
import com.example.analyzer.service.model.SmsHistoryRecord;
import com.example.analyzer.service.model.TariffDataset;
import com.example.analyzer.service.utils.PermissionsUtils;

import java.util.List;

import static androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN;

public final class MainActivity extends AppCompatActivity implements EventListener {
    public final static String TAG = "MainActivityTag";
    private static final String MY_SETTINGS = "my_settings";
    public final static String NAME = "name";
    public final static String GIGABYTE = "gigabyte";
    public final static String SMS = "sms";
    public final static String PRICE = "price";
    public final static String ICON = "icon";
    public final static String COLOR = "color";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] permissions = new String[]{Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_SMS,
                Manifest.permission.CALL_PHONE, Manifest.permission.INTERNET};
        PermissionsUtils.setActivity(this);
        PermissionsUtils.checkAndRequestPermissions(permissions);


        SharedPreferences sp = getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        boolean firstLogin = sp.getBoolean("firstLogin", true);
        if (firstLogin) {
            final InfoFragment infoFragment = new InfoFragment();
            getSupportFragmentManager().beginTransaction().setTransition(TRANSIT_FRAGMENT_OPEN).replace(R.id.main_activity_container, infoFragment).commit();
            return;
        }

        if (savedInstanceState == null) {
            final MainScreenFragment mainScreenFragment = new MainScreenFragment();
            getSupportFragmentManager().beginTransaction().setTransition(TRANSIT_FRAGMENT_OPEN).replace(R.id.main_activity_container, mainScreenFragment).commit();
        }
    }

    @Override
    public void showTariffDifferenceFragment(String name, String gigabyte, String sms, String price, String icon,
                                             int color, List<TariffDataset> tariffs) {
        final Bundle bundle = new Bundle();
        bundle.putString(MainActivity.NAME, name);
        bundle.putString(MainActivity.GIGABYTE, gigabyte);
        bundle.putString(MainActivity.SMS, sms);
        bundle.putString(MainActivity.PRICE, price);
        bundle.putString(MainActivity.ICON, icon);
        bundle.putInt(MainActivity.COLOR, color);

        final TariffDifferenceFragment tariffDifference = new TariffDifferenceFragment();
        tariffDifference.setArguments(bundle);
        tariffDifference.setTariffs(tariffs);

        getSupportFragmentManager().beginTransaction().setTransition(TRANSIT_FRAGMENT_OPEN).replace(R.id.main_activity_container, tariffDifference).addToBackStack(null).commit();
    }

    @Override
    public void showMainFragment() {
        final MainScreenFragment mainScreenFragment = new MainScreenFragment();
        getSupportFragmentManager().beginTransaction().setTransition(TRANSIT_FRAGMENT_OPEN).replace(R.id.main_activity_container, mainScreenFragment).addToBackStack(null).commit();
    }

    @Override
    public void showTariffsFragment() {
        final TariffsFragment tariffsFragment = new TariffsFragment();
        getSupportFragmentManager().beginTransaction().setTransition(TRANSIT_FRAGMENT_OPEN).replace(R.id.main_activity_container, tariffsFragment).addToBackStack(null).commit();
    }

    @Override
    public void showDetailsFragment(List<CallHistoryRecord> calls, List<SmsHistoryRecord> sms) {
        final DetailsFragment detailsFragment = new DetailsFragment(calls, sms);
        getSupportFragmentManager().beginTransaction().setTransition(TRANSIT_FRAGMENT_OPEN).replace(R.id.main_activity_container, detailsFragment).addToBackStack(null).commit();
    }

    @Override
    public void showInfoFragment() {
        final InfoFragment infoFragment = new InfoFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, infoFragment).setTransition(TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
    }
}
