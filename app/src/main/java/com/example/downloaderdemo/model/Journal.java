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


    @Override
    public String toString() {
        return title;
    }
}

