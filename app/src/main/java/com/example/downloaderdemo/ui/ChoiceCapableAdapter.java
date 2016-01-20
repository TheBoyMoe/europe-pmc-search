package com.example.downloaderdemo.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.downloaderdemo.fragment.DownloadFragment.JournalViewHolder;

/**
 * Reference
 * The Busy Coder's Guide to Android Development (https://commonsware.com/Android) p1260-64
 */
public abstract class ChoiceCapableAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>{

    private ChoiceMode mChoiceMode;
    private RecyclerView mRecyclerView;

    public ChoiceCapableAdapter(RecyclerView recyclerView, ChoiceMode choiceMode) {
        super();
        mRecyclerView = recyclerView;
        mChoiceMode = choiceMode;
    }


    public void onChecked(int position, boolean isChecked) {
        if(mChoiceMode.isSingleChoice()) {
            int checked = mChoiceMode.getCheckedPosition();
            if(checked >= 0) {
                JournalViewHolder row =
                        (JournalViewHolder) mRecyclerView.findViewHolderForAdapterPosition(checked);
                if(row != null) {
                    row.setChecked(false);
                }
            }
        }
        mChoiceMode.setChecked(position, isChecked);
    }

    public boolean isChecked(int position) {
        return mChoiceMode.isChecked(position);
    }

    public void onSaveInstanceState(Bundle state) {
        mChoiceMode.onSaveInstanceState(state);
    }

    public void onRestoreInstanceState(Bundle state) {
        mChoiceMode.onRestoreInstanceState(state);
    }

}
