package com.example.downloaderdemo.event;

public class OnListItemClickEvent extends BaseEvent{

    private int mPosition;

    public OnListItemClickEvent(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }
}
