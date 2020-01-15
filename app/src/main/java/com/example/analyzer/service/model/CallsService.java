package com.example.analyzer.service.model;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class CallsService {
    public static final String TAG = "CallsModuleTag";
    private static final String CALL_TYPE_OUTGOING = "OUTGOING";
    private static final String CALL_TYPE_INCOMING = "INCOMING";
    private static final String CALL_TYPE_MISSED = "MISSED";
    private static final String CALL_TYPE_REJECTED = "REJECTED";

    public static List<CallHistoryRecord> getCalls(@NonNull Context context) {
        final String[] Projection = {CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION,
                CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE};
        final String Order = CallLog.Calls.DATE + " DESC ";
        List<CallHistoryRecord> callHistory = new ArrayList<>();

        int permissionCheck = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CALL_LOG);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            return null;
        }

        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, Projection, null, null,
                Order);
        if (cursor == null) {
            return null;
        } else if (cursor.getCount() < 1) {
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
}
