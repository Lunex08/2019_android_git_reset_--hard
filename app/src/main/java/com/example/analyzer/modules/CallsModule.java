package com.example.analyzer.modules;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CallsModule {
    public static final String TAG = "CallsModuleTag";
    private final Context appContext;

    public CallsModule(@NonNull Context appContext) {
        this.appContext = appContext;
    }

    public List<CallHistoryRecord> getCalls() {
        String[] mProjection = {CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION,
                CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE};
        String mSelectionClause = null;
        String[] mSelectionArgs = null;
        String mOrder = CallLog.Calls.DATE + " DESC ";

        int permissionCheck = ContextCompat.checkSelfPermission(this.appContext, Manifest.permission.READ_CALL_LOG);
        if (permissionCheck != appContext.getPackageManager().PERMISSION_GRANTED) {
            Log.e(TAG, "bad123");
            return null;
        }
        List<CallHistoryRecord> callHistory = new ArrayList<>();
        ContentResolver cr = this.appContext.getContentResolver();
        Cursor mCursor = cr.query(CallLog.Calls.CONTENT_URI, mProjection, mSelectionClause, mSelectionArgs, mOrder);

        if (null == mCursor) {
            android.util.Log.e(TAG, "Can't receive data from content provider with URI: " + CallLog.Calls.CONTENT_URI);
            return null;
        } else if (mCursor.getCount() < 1) {
            Toast toast = Toast.makeText(this.appContext, "No calls in history", Toast.LENGTH_SHORT);
            toast.show();
        } else {
            int numberIndex = mCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int dateIndex = mCursor.getColumnIndex(CallLog.Calls.DATE);
            int durationIndex = mCursor.getColumnIndex(CallLog.Calls.DURATION);
            int callNameIndex = mCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int typeIndex = mCursor.getColumnIndex(CallLog.Calls.TYPE);

            while (mCursor.moveToNext()) {
                String phNumber = mCursor.getString(numberIndex);
                String callDuration = mCursor.getString(durationIndex);
                String callName = mCursor.getString(callNameIndex);
                Date callDate = new Date(mCursor.getLong(dateIndex));

                String callType = mCursor.getString(typeIndex);

                switch (Integer.parseInt(callType)) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        callType = "OUTGOING";
                        break;

                    case CallLog.Calls.INCOMING_TYPE:
                        callType = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        callType = "MISSED";
                        break;

                    case CallLog.Calls.REJECTED_TYPE:
                        callType = "REJECTED";
                        break;
                }
                callHistory.add(new CallHistoryRecord(phNumber, callType, callDuration, callName, callDate));
            }
        }
        mCursor.close();
        return callHistory;
    }

    public class CallHistoryRecord {
        String mPhNumber;
        String mType;
        String mDuration;
        String mName;
        Date mDate;

        public CallHistoryRecord(@NonNull String phNumber, @NonNull String type, @NonNull String duration,
                                 @NonNull String name, @NonNull Date date) {
            this.mPhNumber = phNumber;
            this.mType = type;
            this.mDuration = duration;
            this.mName = name;
            this.mDate = date;
        }

        @NonNull
        public String getPhNumber() {
            return mPhNumber;
        }

        @NonNull
        public String getType() {
            return mType;
        }

        @NonNull
        public String getDuration() {
            return mDuration;
        }

        @NonNull
        public String getName() {
            return mName;
        }

        @NonNull
        public Date getDate() {
            return mDate;
        }
    }
}
