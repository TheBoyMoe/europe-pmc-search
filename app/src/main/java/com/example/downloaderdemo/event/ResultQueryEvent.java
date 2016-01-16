package com.example.downloaderdemo.event;

import com.example.downloaderdemo.model.ResultQuery;

public class ResultQueryEvent extends BaseEvent{

    private ResultQuery mResultQuery;

    public ResultQueryEvent(ResultQuery resultQuery) {
        mResultQuery = resultQuery;
    }

    public ResultQuery getResultQuery() {
        return mResultQuery;
    }
}
