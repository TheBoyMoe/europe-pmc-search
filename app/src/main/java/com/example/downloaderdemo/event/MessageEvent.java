package com.example.downloaderdemo.event;

public class MessageEvent extends BaseEvent{

    private String mMessage;

    public MessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
