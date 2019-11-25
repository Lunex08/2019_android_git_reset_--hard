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

    private final String SMS_STATUS_NONE = "none";
    private final String SMS_STATUS_COMPLETE = "sended";
    private final String SMS_STATUS_PENDING = "pending";
    private final String SMS_STATUS_FAILED = "failed";

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

        Cursor Cursor = this.activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, Projection, null, null,
                Order);
        if (null == Cursor) {
            android.util.Log.e(TAG, RECEIVE_ERROR_MESSAGE + CallLog.Calls.CONTENT_URI);
            return null;
        } else if (Cursor.getCount() < 1) {
            Toast.makeText(this.activity, EMPTY_HISTORY_MESSAGE, Toast.LENGTH_SHORT).show();
        } else {
            final int numberIndex = Cursor.getColumnIndex(CallLog.Calls.NUMBER);
            final int dateIndex = Cursor.getColumnIndex(CallLog.Calls.DATE);
            final int durationIndex = Cursor.getColumnIndex(CallLog.Calls.DURATION);
            final int callNameIndex = Cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            final int typeIndex = Cursor.getColumnIndex(CallLog.Calls.TYPE);

            while (Cursor.moveToNext()) {
                final String phNumber = Cursor.getString(numberIndex);
                final String callDuration = Cursor.getString(durationIndex);
                final String callName = Cursor.getString(callNameIndex);
                final Date callDate = new Date(Cursor.getLong(dateIndex));

                String callType = Cursor.getString(typeIndex);

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
        Cursor.close();
        return callHistory;
    }

    public List<SmsHistoryRecord> getSMS() {
        final String SMS_ADDRESS_FIELD_NAME = "address";
        final String SMS_PERSON_FIELD_NAME = "person";
        final String SMS_DATE_FIELD_NAME = "date";
        final String SMS_STATUS_FIELD_NAME = "status";
        final String[] Projection = {SMS_ADDRESS_FIELD_NAME, SMS_PERSON_FIELD_NAME, SMS_DATE_FIELD_NAME,
                SMS_STATUS_FIELD_NAME};
        final String Order = " date DESC ";
        List<SmsHistoryRecord> smsHistory = new ArrayList<>();

        int permissionCheck = ContextCompat.checkSelfPermission(activity.getApplicationContext(),
                Manifest.permission.READ_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, NO_PERMISSION_GRANTED_MESSAGE + Manifest.permission.READ_SMS);
            return null;
        }


        Cursor Cursor = this.activity.getContentResolver().query(Uri.parse(SMS_PROVIDER_URI), Projection, null, null,
                Order);
        if (null == Cursor) {
            android.util.Log.e(TAG, RECEIVE_ERROR_MESSAGE + CallLog.Calls.CONTENT_URI);
            return null;
        } else if (Cursor.getCount() < 1) {
            final String EMPTY_SMS_HISTORY_MESSAGE = "No sms";
            Toast.makeText(this.activity, EMPTY_SMS_HISTORY_MESSAGE, Toast.LENGTH_SHORT).show();
        } else {
            final int addressIndex = Cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS);
            final int personIndex = Cursor.getColumnIndexOrThrow(Telephony.Sms.PERSON);
            final int dateIndex = Cursor.getColumnIndexOrThrow(Telephony.Sms.DATE);
            final int statusIndex = Cursor.getColumnIndexOrThrow(Telephony.Sms.STATUS);

            while (Cursor.moveToNext()) {
                final String address = Cursor.getString(addressIndex);
                final String person = Cursor.getString(personIndex);
                final Date date = new Date(Cursor.getLong(dateIndex));
                String status = Cursor.getString(statusIndex);

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
        Cursor.close();
        return smsHistory;
    }
}
