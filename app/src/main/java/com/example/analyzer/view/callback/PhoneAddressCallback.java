package com.example.analyzer.view.callback;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneAddressCallback extends TelephonyManager.UssdResponseCallback {
    private final Context context;
    private TextView nubmer;

    public PhoneAddressCallback(Context context, TextView number) {
        super();
        this.nubmer = number;
        this.context = context;
    }

    @Override
    public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
        super.onReceiveUssdResponse(telephonyManager, request, response);
        final Pattern numberPattern = Pattern.compile("\\+\\d{11}?");
        Matcher matcher = numberPattern.matcher(response.toString());
        if (matcher.find()) {
            String res = matcher.group(0);
            this.nubmer.setText(res);
        }
    }

    @Override
    public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
        super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
        Toast.makeText(this.context, String.valueOf(failureCode), Toast.LENGTH_SHORT).show();
    }
};