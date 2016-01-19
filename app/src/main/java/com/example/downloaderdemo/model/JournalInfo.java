package com.example.downloaderdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class JournalInfo implements Parcelable {

    private String issue;
    private String volume;
    private long journalIssueId;
    private int yearOfPublication;
    private Journal journal;

    public JournalInfo() {}

    public String getIssue() {
        return issue;
    }

    public String getVolume() {
        return volume;
    }

    public long getJournalIssueId() {
        return journalIssueId;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public Journal getJournal() {
        return journal;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.issue);
        dest.writeString(this.volume);
        dest.writeLong(this.journalIssueId);
        dest.writeInt(this.yearOfPublication);
        dest.writeParcelable(this.journal, 0);
    }

    protected JournalInfo(Parcel in) {
        this.issue = in.readString();
        this.volume = in.readString();
        this.journalIssueId = in.readLong();
        this.yearOfPublication = in.readInt();
        this.journal = in.readParcelable(Journal.class.getClassLoader());
    }

    public static final Parcelable.Creator<JournalInfo> CREATOR = new Parcelable.Creator<JournalInfo>() {
        public JournalInfo createFromParcel(Parcel source) {
            return new JournalInfo(source);
        }

        public JournalInfo[] newArray(int size) {
            return new JournalInfo[size];
        }
    };
}
