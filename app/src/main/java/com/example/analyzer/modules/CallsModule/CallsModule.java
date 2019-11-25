package com.example.analyzer.modules.CallsModule;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class CallsModule {
    private static final String SMS_PROVIDER_URI = "content://sms/inbox";

    private static final String TAG = "CallsModuleTag";
    private static final String CALL_TYPE_OUTGOING = "OUTGOING";
    private static final String CALL_TYPE_INCOMING = "INCOMING";
    private static final String CALL_TYPE_MISSED = "MISSED";
    private static final String CALL_TYPE_REJECTED = "REJECTED";

    private static final String NO_PERMISSION_GRANTED_MESSAGE = "No permission granted on ";
    private static final String RECEIVE_ERROR_MESSAGE = "Can't receive data from content provider with URI ";
    private static final String EMPTY_HISTORY_MESSAGE = "No calls in history";

    private static final int STATUS_NONE = -1;
    private static final int STATUS_COMPLETE = 0;
    private static final int STATUS_PENDING = 32;
    private static final int STATUS_FAILED = 64;


    private final Activity activity;

    public CallsModule(@NonNull Activity activity) {
        this.activity = activity;
    }

    public List<CallHistoryRecord> getCalls() {
        final String[] Projection = {CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION,
                CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE};
        final String Order = CallLog.Calls.DATE + " DESC ";
        List<CallHistoryRecord> callHistory = new ArrayList<>();

        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_CALL_LOG);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, NO_PERMISSION_GRANTED_MESSAGE + Manifest.permission.READ_CALL_LOG);
            return null;
        }

        Cursor cursor = this.activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, Projection, null, null,
                Order);
        if (null == cursor) {
            android.util.Log.e(TAG, RECEIVE_ERROR_MESSAGE + CallLog.Calls.CONTENT_URI);
            return null;
        } else if (cursor.getCount() < 1) {
            Toast.makeText(this.activity, EMPTY_HISTORY_MESSAGE, Toast.LENGTH_SHORT).show();
        } else {
            final int numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            final int dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE);
            final int durationIndex = cursor.getColumnIndex(CallLog.Calls.DURATION);
            final int callNameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            final int typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE);

            while (cursor.moveToNext()) {
                final String phNumber = cursor.getString(numberIndex);
                final String callDuration = cursor.getString(durationIndex);
                final String callName = cursor.getString(callNameIndex);
                final Date callDate = new Date(cursor.getLong(dateIndex));

                String callType = cursor.getString(typeIndex);

                switch (Integer.parseInt(callType)) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = CALL_TYPE_OUTGOING;
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        callType = CALL_TYPE_INCOMING;
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        callType = CALL_TYPE_MISSED;
                        break;

                    case CallLog.Calls.REJECTED_TYPE:
                        callType = CALL_TYPE_REJECTED;
                        break;
                }
                callHistory.add(new CallHistoryRecord(phNumber, callType, callDuration, callName, callDate));
            }
        }
        cursor.close();
        return callHistory;
    }

    public List<SmsHistoryRecord> getSMS() {
        final String SMS_ADDRESS_FIELD_NAME = "address";
        final String SMS_PERSON_FIELD_NAME = "person";
        final String SMS_DATE_FIELD_NAME = "date";
        final String SMS_STATUS_FIELD_NAME = "status";
        final String[] Projection = {SMS_ADDRESS_FIELD_NAME, SMS_PERSON_FIELD_NAME, SMS_DATE_FIELD_NAME,
                SMS_STATUS_FIELD_NAME};
        final String Order = SMS_DATE_FIELD_NAME + " DESC ";
        List<SmsHistoryRecord> smsHistory = new ArrayList<>();

        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, NO_PERMISSION_GRANTED_MESSAGE + Manifest.permission.READ_SMS);
            return null;
        }


        Cursor cursor = this.activity.getContentResolver().query(Uri.parse(SMS_PROVIDER_URI), Projection, null, null,
                Order);
        if (null == cursor) {
            android.util.Log.e(TAG, RECEIVE_ERROR_MESSAGE + CallLog.Calls.CONTENT_URI);
            return null;
        } else if (cursor.getCount() < 1) {
            final String EMPTY_SMS_HISTORY_MESSAGE = "No sms";
            Toast.makeText(this.activity, EMPTY_SMS_HISTORY_MESSAGE, Toast.LENGTH_SHORT).show();
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

                String SMS_STATUS_NONE = "none";
                String SMS_STATUS_COMPLETE = "sended";
                String SMS_STATUS_PENDING = "pending";
                String SMS_STATUS_FAILED = "failed";

                switch (Integer.parseInt(status)) {
                    case STATUS_NONE:
                        status = SMS_STATUS_NONE;
                        break;

                    case STATUS_COMPLETE:
                        status = SMS_STATUS_COMPLETE;
                        break;

                    case STATUS_PENDING:
                        status = SMS_STATUS_PENDING;
                        break;

                    case STATUS_FAILED:
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
