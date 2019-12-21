package com.example.analyzer.modules.DataModule;

import androidx.annotation.NonNull;

import java.util.Date;

public class SmsHistoryRecord {
    private final String mAddress;
    private final String mPerson;
    private final String mStatus;
    private final Date mDate;

    SmsHistoryRecord(@NonNull String address, @NonNull String person, @NonNull Date date, @NonNull String status) {
        this.mAddress = address;
        this.mPerson = person;
        this.mDate = date;
        this.mStatus = status;
    }

    @NonNull
    public String getPhNumber() {
        return mAddress;
    }

    @NonNull
    public String getType() {
        return mStatus;
    }

    @NonNull
    public String getPerson() {
        return mPerson;
    }

    @NonNull
    public Date getDate() {
        return mDate;
    }
}