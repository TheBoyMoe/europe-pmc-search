package com.example.downloaderdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class KeywordList implements Parcelable {

    private List<String> keyword;

    public KeywordList() {  }

    public List<String> getKeyword() {
        return keyword;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.keyword);
    }

    protected KeywordList(Parcel in) {
        this.keyword = in.createStringArrayList();
    }

    public static final Parcelable.Creator<KeywordList> CREATOR = new Parcelable.Creator<KeywordList>() {
        public KeywordList createFromParcel(Parcel source) {
            return new KeywordList(source);
        }

        public KeywordList[] newArray(int size) {
            return new KeywordList[size];
        }
    };
}
