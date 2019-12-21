package com.example.analyzer.modules.DataModule;

import androidx.annotation.NonNull;

import java.util.Date;

public class SmsHistoryRecord {
    private final String mAddress;
    private final String mPerson;
    private final String mStatus;
    private final Date mDate;

    SmsHistoryRecord(@NonNull String address, @NonNull String person, @NonNull Date date, @NonNull String status) {
        mAddress = address;
        mPerson = person;
        mDate = date;
        mStatus = status;
    }

    @NonNull
    public String getAddress() {
        return mAddress;
    }

    @NonNull
    public String getStatus() {
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