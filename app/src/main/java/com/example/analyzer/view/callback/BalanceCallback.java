package com.example.analyzer.view.callback;

import android.telephony.TelephonyManager;

import com.example.analyzer.service.repository.PassParam;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BalanceCallback extends TelephonyManager.UssdResponseCallback {
    private static final String BALANCE_FORMAT = "%.2f";
    PassParam passParam;

    public BalanceCallback(PassParam passParam) {
        super();
        this.passParam = passParam;
    }

    @Override
    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
        final Pattern balancePattern = Pattern.compile("\\d+(.\\d+)?");
        Matcher matcher = balancePattern.matcher(response.toString());
        if (matcher.find()) {
            String rawNumber = matcher.group(0);
            rawNumber = rawNumber != null ? rawNumber : "0";
            if (response.toString().contains("Минус")) {
                rawNumber = "-" + rawNumber;
            }
            rawNumber = rawNumber.replace(",", ".");

            float balanceValue = Float.parseFloat(rawNumber);
            String balanceValueString = String.format(BALANCE_FORMAT, balanceValue).replace(",", ".");
            passParam.setParam(balanceValueString);
        }
    }

    @Override
    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
        super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
        passParam.setParam("FAIL");
    }
}
