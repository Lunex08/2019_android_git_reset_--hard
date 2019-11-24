package com.example.analyzer.modules.CallsModule;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class CallsModule {
    public static final String TAG = "CallsModuleTag";
    private static final String CALL_TYPE_OUTGOING = "OUTGOING";
    private static final String CALL_TYPE_INCOMING = "INCOMING";
    private static final String CALL_TYPE_MISSED = "MISSED";
    private static final String CALL_TYPE_REJECTED = "REJECTED";

    private static final String NO_PERMISSION_GRANTED_MESSAGE = "No permission granted on ";
    private static final String RECEIVE_ERROR_MESSAGE = "Can't receive data from content provider with URI ";
    private static final String EMPTY_HISTORY_MESSAGE = "No calls in history";


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
}