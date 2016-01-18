package com.example.downloaderdemo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.model.Journal;

import timber.log.Timber;

public class DetailFragment extends BaseFragment{

    public static final String JOURNAL_ITEM = "journal";
    private Journal mJournal;

    public DetailFragment() {}

    public static DetailFragment newInstance(Journal journal) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(JOURNAL_ITEM, journal);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJournal = getArguments().getParcelable(JOURNAL_ITEM);
        if(mJournal != null)
            Timber.i("Journal item %s", mJournal);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView articleTitle = (TextView) view.findViewById(R.id.article_title);
        articleTitle.setText(mJournal.getTitle());
        return view;
    }



}
