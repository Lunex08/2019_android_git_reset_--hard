package com.example.analyzer.modules.DataModule;

import androidx.annotation.NonNull;

import java.util.Date;

public final class CallHistoryRecord {
    private final String mPhNumber;
    private final String mType;
    private final String mDuration;
    private final String mName;
    private final Date mDate;

    CallHistoryRecord(@NonNull String phNumber, @NonNull String type, @NonNull String duration, @NonNull String name,
                      @NonNull Date date) {
        mPhNumber = phNumber;
        mType = type;
        mDuration = duration;
        mName = name;
        mDate = date;
    }

    @NonNull
    public String getAddress() {
        return mPhNumber;
    }

    @NonNull
    public String getStatus() {
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
