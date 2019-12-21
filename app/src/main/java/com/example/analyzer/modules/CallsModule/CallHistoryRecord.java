package com.example.analyzer.modules.CallsModule;

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
