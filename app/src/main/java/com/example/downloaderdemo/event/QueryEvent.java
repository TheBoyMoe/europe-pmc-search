package com.example.downloaderdemo.event;

public class QueryEvent extends BaseEvent{

    private String mQuery;

    public QueryEvent(String query) {
        mQuery = query;
    }

    public String getQuery() {
        return mQuery;
    }
}
