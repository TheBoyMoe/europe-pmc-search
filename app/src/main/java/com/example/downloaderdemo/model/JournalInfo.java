package com.example.downloaderdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class JournalInfo implements Parcelable {

    private String issue;
    private String volume;
    private Long journalIssueId;
    private Long yearOfPublication;
    private Journal journal;

    public JournalInfo() {}

    public String getIssue() {
        return issue;
    }

    public String getVolume() {
        return volume;
    }

    public Long getJournalIssueId() {
        return journalIssueId;
    }

    public Long getYearOfPublication() {
        return yearOfPublication;
    }

    public Journal getJournal() {
        return journal;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public void setYearOfPublication(Long yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.issue);
        dest.writeString(this.volume);
        dest.writeValue(this.journalIssueId);
        dest.writeValue(this.yearOfPublication);
        dest.writeParcelable(this.journal, 0);
    }

    protected JournalInfo(Parcel in) {
        this.issue = in.readString();
        this.volume = in.readString();
        this.journalIssueId = (Long) in.readValue(Long.class.getClassLoader());
        this.yearOfPublication = (Long) in.readValue(Long.class.getClassLoader());
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
