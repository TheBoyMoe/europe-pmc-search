package com.example.downloaderdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Journal implements Parcelable {

    private String title;
    private String essn;
    private String issn;

    public Journal() {  }

    public String getTitle() {
        return title;
    }

    public String getEssn() {
        return essn;
    }

    public String getIssn() {
        return issn;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.essn);
        dest.writeString(this.issn);
    }

    protected Journal(Parcel in) {
        this.title = in.readString();
        this.essn = in.readString();
        this.issn = in.readString();
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
