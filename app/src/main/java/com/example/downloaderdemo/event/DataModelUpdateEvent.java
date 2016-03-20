package com.example.downloaderdemo.event;

public class  DataModelUpdateEvent extends BaseEvent{

    private boolean mDataModelUpdate;

    public DataModelUpdateEvent(boolean dataModelUpdate) {
        mDataModelUpdate = dataModelUpdate;
    }

    public boolean isDataModelUpToDate() {
        return mDataModelUpdate;
    }
}
