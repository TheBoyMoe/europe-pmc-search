package com.example.downloaderdemo.model;

public class Journal {

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
}

