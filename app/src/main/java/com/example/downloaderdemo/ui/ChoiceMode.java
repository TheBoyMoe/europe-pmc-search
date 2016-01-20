package com.example.downloaderdemo.ui;

import android.os.Bundle;

/**
 * Reference
 * The Busy Coder's Guide to Android Development (https://commonsware.com/Android) p1260-64
 */
public interface ChoiceMode {

    boolean isSingleChoice();
    int getCheckedPosition();
    void setChecked(int position, boolean isChecked);
    boolean isChecked(int position);
    void onSaveInstanceState(Bundle state);
    void onRestoreInstanceState(Bundle state);

}
