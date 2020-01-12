package com.example.analyzer.service.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Telephony;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.analyzer.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsService {
    public static final String TAG = "SmsModuleTag";

    private static final String SMS_PROVIDER_URI = "content://sms/inbox";

    private static final String SMS_STATUS_NONE = "none";
    private static final String SMS_STATUS_COMPLETE = "sended";
    private static final String SMS_STATUS_PENDING = "pending";
    private static final String SMS_STATUS_FAILED = "failed";

    private static final int SMS_STATUS_NONE_CODE = -1;
    private static final int SMS_STATUS_COMPLETE_CODE = 0;
    private static final int SMS_STATUS_PENDING_CODE = 32;
    private static final int SMS_STATUS_FAILED_CODE = 64;

    public static List<SmsHistoryRecord> getSms(@NonNull Context context) {
        final String SMS_ADDRESS_FIELD_NAME = "address";
        final String SMS_PERSON_FIELD_NAME = "person";
        final String SMS_DATE_FIELD_NAME = "date";
        final String SMS_STATUS_FIELD_NAME = "status";
        final String[] Projection = {SMS_ADDRESS_FIELD_NAME, SMS_PERSON_FIELD_NAME, SMS_DATE_FIELD_NAME,
                SMS_STATUS_FIELD_NAME};
        final String Order = SMS_DATE_FIELD_NAME + " DESC ";
        List<SmsHistoryRecord> smsHistory = new ArrayList<>();

        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //            Log.e(TAG,
            //                    activity.getResources().getString(R.string.no_permission_granted_message) +
            //                    Manifest.permission.READ_SMS);
            return null;
        }

        Cursor cursor = context.getContentResolver().query(Uri.parse(SMS_PROVIDER_URI), Projection, null, null,
                Order);
        if (null == cursor) {
            android.util.Log.e(TAG,
                    context.getResources().getString(R.string.receive_error_message) + CallLog.Calls.CONTENT_URI);
            return null;
        } else if (cursor.getCount() < 1) {
            Toast.makeText(context, context.getResources().getString(R.string.empty_sms_history_message),
                    Toast.LENGTH_SHORT).show();
        } else {
            final int addressIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS);
            final int personIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.PERSON);
            final int dateIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.DATE);
            final int statusIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.STATUS);

            while (cursor.moveToNext()) {
                final String address = cursor.getString(addressIndex);
                final String person = cursor.getString(personIndex);
                final Date date = new Date(cursor.getLong(dateIndex));
                String status = cursor.getString(statusIndex);

                switch (Integer.parseInt(status)) {
                    case SMS_STATUS_NONE_CODE:
                        status = SMS_STATUS_NONE;
                        break;
                    case SMS_STATUS_COMPLETE_CODE:
                        status = SMS_STATUS_COMPLETE;
                        break;
                    case SMS_STATUS_PENDING_CODE:
                        status = SMS_STATUS_PENDING;
                        break;
                    case SMS_STATUS_FAILED_CODE:
                        status = SMS_STATUS_FAILED;
                        break;
                }
                smsHistory.add(new SmsHistoryRecord(address, person, date, status));
            }
        }
        cursor.close();

        return smsHistory;
    }
}
