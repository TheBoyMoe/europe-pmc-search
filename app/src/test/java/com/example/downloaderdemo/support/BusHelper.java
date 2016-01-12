package com.example.downloaderdemo.support;

import com.example.downloaderdemo.event.BaseEvent;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class BusHelper {

    // all events are cached to the array list
    private List<BaseEvent> mEvents = new ArrayList<>();

    // retrieve the last item from the array
    public BaseEvent getLastEvent() {
        if(!mEvents.isEmpty()) {
            return mEvents.get(mEvents.size() - 1);
        }
        return null;
    }

    // all events posted to the bus are automatically added to the array by Otto
    @Subscribe
    public void onAnyEvent(BaseEvent event) {
        mEvents.add(event);
    }


    public int getEventListSize() {
        return mEvents.size();
    }

}
