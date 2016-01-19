package com.example.downloaderdemo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Article implements Parcelable {

    private String id;
    private String source;
    private String pmid;
    private String title;
    private String authorString;
    private JournalInfo journalInfo;
    private String pageInfo;
    private String abstractText;
    private String affiliation;
    private String language;
    private KeywordList keywordList;
    private String inEPMC;
    private String inPMC;
    private String hasPDF;
    private int citedByCount;

    public Article() {  }

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

    public JournalInfo getJournalInfo() {
        return journalInfo;
    }

    public String getPageInfo() {
        return pageInfo;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getLanguage() {
        return language;
    }

    public KeywordList getKeywordList() {
        return keywordList;
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
        dest.writeParcelable(this.journalInfo, flags);
        dest.writeString(this.pageInfo);
        dest.writeString(this.abstractText);
        dest.writeString(this.affiliation);
        dest.writeString(this.language);
        dest.writeParcelable(this.keywordList, flags);
        dest.writeString(this.inEPMC);
        dest.writeString(this.inPMC);
        dest.writeString(this.hasPDF);
        dest.writeInt(this.citedByCount);
    }

    protected Article(Parcel in) {
        this.id = in.readString();
        this.source = in.readString();
        this.pmid = in.readString();
        this.title = in.readString();
        this.authorString = in.readString();
        this.journalInfo = in.readParcelable(JournalInfo.class.getClassLoader());
        this.pageInfo = in.readString();
        this.abstractText = in.readString();
        this.affiliation = in.readString();
        this.language = in.readString();
        this.keywordList = in.readParcelable(KeywordList.class.getClassLoader());
        this.inEPMC = in.readString();
        this.inPMC = in.readString();
        this.hasPDF = in.readString();
        this.citedByCount = in.readInt();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };


}

