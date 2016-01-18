package com.example.downloaderdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Journal implements Parcelable {

    private String id;
    private String source;
    private String pmid;
    private String title;
    private String authorString;
    private String journalTitle;
    private String issue;
    private String journalVolume;
    private String pubYear;
    private String journalIssn;
    private String pageInfo;
    private String pubType;
    private String inEPMC;
    private String inPMC;
    private String hasPDF;
    private int citedByCount;

    public Journal() {  }

    public String getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getPmid() {
        return pmid;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorString() {
        return authorString;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public String getIssue() {
        return issue;
    }

    public String getJournalVolume() {
        return journalVolume;
    }

    public String getPubYear() {
        return pubYear;
    }

    public String getJournalIssn() {
        return journalIssn;
    }

    public String getPageInfo() {
        return pageInfo;
    }

    public String getPubType() {
        return pubType;
    }

    public String getInEPMC() {
        return inEPMC;
    }

    public String getInPMC() {
        return inPMC;
    }

    public String getHasPDF() {
        return hasPDF;
    }

    public int getCitedByCount() {
        return citedByCount;
    }

    @Override
    public String toString() {
        return title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.source);
        dest.writeString(this.pmid);
        dest.writeString(this.title);
        dest.writeString(this.authorString);
        dest.writeString(this.journalTitle);
        dest.writeString(this.issue);
        dest.writeString(this.journalVolume);
        dest.writeString(this.pubYear);
        dest.writeString(this.journalIssn);
        dest.writeString(this.pageInfo);
        dest.writeString(this.pubType);
        dest.writeString(this.inEPMC);
        dest.writeString(this.inPMC);
        dest.writeString(this.hasPDF);
        dest.writeInt(this.citedByCount);
    }

    protected Journal(Parcel in) {
        this.id = in.readString();
        this.source = in.readString();
        this.pmid = in.readString();
        this.title = in.readString();
        this.authorString = in.readString();
        this.journalTitle = in.readString();
        this.issue = in.readString();
        this.journalVolume = in.readString();
        this.pubYear = in.readString();
        this.journalIssn = in.readString();
        this.pageInfo = in.readString();
        this.pubType = in.readString();
        this.inEPMC = in.readString();
        this.inPMC = in.readString();
        this.hasPDF = in.readString();
        this.citedByCount = in.readInt();
    }

    public static final Parcelable.Creator<Journal> CREATOR = new Parcelable.Creator<Journal>() {
        public Journal createFromParcel(Parcel source) {
            return new Journal(source);
        }

        public Journal[] newArray(int size) {
            return new Journal[size];
        }
    };


}

