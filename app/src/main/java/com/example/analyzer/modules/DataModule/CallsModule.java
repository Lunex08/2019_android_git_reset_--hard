package com.example.analyzer.modules.DataModule;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.analyzer.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class CallsModule {
    public static final String TAG = "CallsModuleTag";
    private static final String CALL_TYPE_OUTGOING = "OUTGOING";
    private static final String CALL_TYPE_INCOMING = "INCOMING";
    private static final String CALL_TYPE_MISSED = "MISSED";
    private static final String CALL_TYPE_REJECTED = "REJECTED";

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
            Log.e(TAG,
                    activity.getResources().getString(R.string.no_permission_granted_message) + Manifest.permission.READ_CALL_LOG);
            return null;
        }

        Cursor cursor = this.activity.getContentResolver().query(CallLog.Calls.CONTENT_URI, Projection, null, null,
                Order);
        if (cursor == null) {
            android.util.Log.e(TAG,
                    activity.getResources().getString(R.string.receive_error_message) + CallLog.Calls.CONTENT_URI);
            return null;
        } else if (cursor.getCount() < 1) {
            Toast.makeText(this.activity, activity.getResources().getString(R.string.empty_calls_history_message),
                    Toast.LENGTH_SHORT).show();
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
