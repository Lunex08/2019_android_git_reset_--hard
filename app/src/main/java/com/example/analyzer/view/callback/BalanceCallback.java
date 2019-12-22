package com.example.analyzer.view.callback;

import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.analyzer.service.repository.PassParam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BalanceCallback extends TelephonyManager.UssdResponseCallback {
    private static final String BALANCE_FORMAT = "%.2f₽";
    PassParam passParam;

    public BalanceCallback(PassParam passParam) {
        super();
        this.passParam = passParam;
    }

    @Override
    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
        super.onReceiveUssdResponse(telephonyManager, request, response);
        final Pattern balancePattern = Pattern.compile("\\d+");
        Matcher matcher = balancePattern.matcher(response.toString());
        if (matcher.find()) {
            Log.d("USSD: ", "test");
            String rawnumber = matcher.group(0);
            Float f = Float.parseFloat(rawnumber != null ? rawnumber : "0");
            passParam.setParam(String.format(BALANCE_FORMAT, f));
        }
    }

    @Override
    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
        super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
        Log.d("USSD resp fail: ", request + String.valueOf(failureCode));
    }
}
