package com.example.downloaderdemo;

import android.app.Application;

import com.example.downloaderdemo.event.BaseEvent;
import com.squareup.otto.Bus;

public class DownloaderDemoApplication extends Application{


    private static DownloaderDemoApplication sInstance;
    private static Bus sBus;

    public static DownloaderDemoApplication getInstance() {
        if(sInstance == null) {
            sInstance = new DownloaderDemoApplication();
            sBus = new Bus();
        }
        return sInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }

    public Bus getBus() {
        return sBus;
    }

    // post any type of event from anywhere in the app to the bus
    public static void postToBus(BaseEvent event) {
        getInstance().getBus().post(event);
    }

}
