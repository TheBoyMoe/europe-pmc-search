package com.example.downloaderdemo;

import android.app.Application;

public class DownloaderDemoApplication extends Application{


    private static DownloaderDemoApplication sInstance;

    public static DownloaderDemoApplication getInstance() {
        if(sInstance == null) {
            sInstance = new DownloaderDemoApplication();
        }
        return sInstance;
    }


}
