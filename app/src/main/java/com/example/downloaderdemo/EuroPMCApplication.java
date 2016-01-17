package com.example.downloaderdemo;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.example.downloaderdemo.event.BaseEvent;
import com.squareup.otto.Bus;

import timber.log.Timber;

public class EuroPMCApplication extends Application{


    private static EuroPMCApplication sInstance;
    private static Bus sBus;

    public static EuroPMCApplication getInstance() {
        if(sInstance == null) {
            sInstance = new EuroPMCApplication();
            sBus = new AndroidBus();
        }
        return sInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        // enable Timber logging
        if(BuildConfig.DEBUG)
            Timber.plant(new Timber.DebugTree());
    }

    public Bus getBus() {
        return sBus;
    }


    // post any type of event from anywhere in the app to the bus
    public static void postToBus(BaseEvent event) {
        getInstance().getBus().post(event);
    }


    // enables posting from UI or background threads
    public static class AndroidBus extends Bus {
        private final Handler mainThread = new Handler(Looper.getMainLooper());

        @Override
        public void post(final Object event) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            } else {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        post(event);
                    }
                });
            }
        }
    }

}
