package com.example.downloaderdemo.event;

import com.example.downloaderdemo.model.ResultQuery;

public class QueryCompletionEvent extends BaseEvent{

    private ResultQuery mResultQuery;

    public QueryCompletionEvent(ResultQuery resultQuery) {
        mResultQuery = resultQuery;
    }

    public ResultQuery getResultQuery() {
        return mResultQuery;
    }
}
