package com.example.downloaderdemo.event;

import com.example.downloaderdemo.model.Journal;

public class OnListItemClickEvent extends BaseEvent{

    private Journal mJournal;

    public OnListItemClickEvent(Journal journal) {
        mJournal = journal;
    }

    public Journal getJournal() {
        return mJournal;
    }
}
