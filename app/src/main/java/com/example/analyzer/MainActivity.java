package com.example.analyzer;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.analyzer.fragments.DetailsFragment;
import com.example.analyzer.fragments.InfoFragment;
import com.example.analyzer.fragments.MainScreenFragment;
import com.example.analyzer.fragments.TariffDifferenceFragment;
import com.example.analyzer.fragments.TariffsFragment;
import com.example.analyzer.utils.PermissionsUtils;


public final class MainActivity extends AppCompatActivity implements MainScreenFragment.EventListener {
    public final static String TAG = "MainActivityTag";
    private static final String MY_SETTINGS = "my_settings";
    public final static String NAME = "name";
    public final static String GIGABYTE = "gigabyte";
    public final static String SMS = "sms";
    public final static String PRICE = "price";
    public final static String ICON = "icon";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String[] permissions = new String[]{
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_SMS,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.INTERNET
        };
        PermissionsUtils.checkAndRequestPermissions(this, permissions);

        SharedPreferences sp = getSharedPreferences(MY_SETTINGS, Context.MODE_PRIVATE);
        boolean firstLogin = sp.getBoolean("firstLogin", true);
        if (firstLogin) {
            final InfoFragment infoFragment = InfoFragment.getInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, infoFragment).commit();
            return;
        }

        if (savedInstanceState == null) {
            final MainScreenFragment mainScreenFragment = MainScreenFragment.getInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity_container, mainScreenFragment).commit();
        }
    }

    @Override
    public void onItemClick(int dest) {
        final DetailsFragment detailsFragment;

        switch (dest) {
            case R.string.to_detail:
                detailsFragment = new DetailsFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_activity_container, detailsFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.string.to_tariffs:
                final TariffsFragment tariffsFragment = new TariffsFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_activity_container, tariffsFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case R.string.to_settings:
                final InfoFragment infoFragment = new InfoFragment();

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_activity_container, infoFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    public void onTariffClick(String name, String gigabyte, String sms, String price, String icon) {
        final Bundle bundle = new Bundle();
        bundle.putString(MainActivity.NAME, name);
        bundle.putString(MainActivity.GIGABYTE, gigabyte);
        bundle.putString(MainActivity.SMS, sms);
        bundle.putString(MainActivity.PRICE, price);
        bundle.putString(MainActivity.ICON, icon);

        final TariffDifferenceFragment tariffDifference = new TariffDifferenceFragment();
        tariffDifference.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_container, tariffDifference)
                .addToBackStack(null)
                .commit();
    }
}
