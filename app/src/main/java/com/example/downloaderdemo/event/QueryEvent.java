package com.example.downloaderdemo.event;

public class QueryEvent extends BaseEvent{

    private String mQuery;
    private int mPageNumber;

    public QueryEvent(String query, int pageNumber) {
        mQuery = query;
        mPageNumber = pageNumber;
    }

    public String getQuery() {
        return mQuery;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

}
