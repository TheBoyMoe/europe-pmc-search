package com.example.downloaderdemo.ui;


import android.os.Bundle;

/**
 * Reference
 * The Busy Coder's Guide to Android Development (https://commonsware.com/Android) p1260-64
 */
public class SingleChoiceMode implements ChoiceMode{

    private static final String STATE_CHECKED = "checked state";
    private int mCheckedPosition = -1;

    @Override
    public boolean isSingleChoice() {
        return true;
    }

    @Override
    public int getCheckedPosition() {
        return mCheckedPosition;
    }

    @Override
    public void setChecked(int position, boolean isChecked) {
        if(isChecked) {
            mCheckedPosition = position;
        } else if(isChecked(position)){
            mCheckedPosition = -1;
        }
    }

    @Override
    public boolean isChecked(int position) {
        return mCheckedPosition == position;
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        state.putInt(STATE_CHECKED, mCheckedPosition);
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        mCheckedPosition = state.getInt(STATE_CHECKED, -1);
    }
}
