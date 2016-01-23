package com.example.downloaderdemo.event;

public class RefreshAdapterEvent extends BaseEvent{

    private boolean mRefreshAdapter;

    public RefreshAdapterEvent(boolean refreshAdapter) {
        mRefreshAdapter = refreshAdapter;
    }

    public boolean isRefreshAdapter() {
        return mRefreshAdapter;
    }
}
