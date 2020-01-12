package com.example.analyzer.service.repository;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.telephony.TelephonyManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.analyzer.service.utils.PermissionsUtils;
import com.example.analyzer.view.callback.BalanceCallback;

import java.util.Objects;

public class BalanceRepository implements PassParam {
    private static final BalanceRepository mInstance = new BalanceRepository();
    private static MutableLiveData<String> data;

    private static final String USER_SPECIFIC_USSD_GET_BALANCE = "*100#";
    private static final String USER_SPECIFIC_USSD_GET_NUMBER = "*103#";

    public static BalanceRepository getInstance() {
        return mInstance;
    }

    public BalanceRepository() {
        data = new MutableLiveData<>();
    }

    public LiveData<String> getBalance() {
        return data;
    }

    @Override
    public void setParam(String balance) {
        try {
            data.setValue(balance);
        } catch (NumberFormatException ignored) {
        }
    }

    public void refreshBalance(Context context) {
        TelephonyManager telephonyManager =
                (TelephonyManager) Objects.requireNonNull(context).getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyManager.UssdResponseCallback balanceCallback = new BalanceCallback(this);

        if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            assert telephonyManager != null;
            telephonyManager.sendUssdRequest(USER_SPECIFIC_USSD_GET_BALANCE, balanceCallback, new Handler());
        }
    }

    private int checkSelfPermission(String permission) {
        return PermissionsUtils.checkAndRequestPermissions(new String[] {permission});
    }
}
