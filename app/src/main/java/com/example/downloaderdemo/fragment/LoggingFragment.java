package com.example.downloaderdemo.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import timber.log.Timber;

public class LoggingFragment extends Fragment{

    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Timber.i("%s onAttach called", TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.i("%s onCreate called", TAG);
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.i("%s onStart called", TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        Timber.i("%s onResume called", TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.i("%S onPause called", TAG);
    }

    @Override
    public void onStop() {
        super.onStop();
        Timber.i("%s onStop called", TAG);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("%s onDestroy called", TAG);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Timber.i("%s onDetach called", TAG);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.i("%s onCreateView called", TAG);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
