package com.example.downloaderdemo.event;

public class IsThreadRunningEvent extends BaseEvent{

    private boolean mIsThreadRunning;

    public IsThreadRunningEvent(boolean isThreadRunning) {
        mIsThreadRunning = isThreadRunning;
    }

    public boolean isThreadRunning() {
        return mIsThreadRunning;
    }
}
