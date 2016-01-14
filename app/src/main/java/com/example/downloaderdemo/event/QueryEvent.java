package com.example.downloaderdemo.event;

import com.example.downloaderdemo.model.ResultQuery;

public class QueryEvent extends BaseEvent{

    private ResultQuery mResultQuery;

    public QueryEvent(ResultQuery resultQuery) {
        mResultQuery = resultQuery;
    }

    public ResultQuery getResultQuery() {
        return mResultQuery;
    }
}
