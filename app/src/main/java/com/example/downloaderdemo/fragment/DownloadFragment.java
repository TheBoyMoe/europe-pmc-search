package com.example.downloaderdemo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.event.QueryEvent;
import com.example.downloaderdemo.model.Journal;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import timber.log.Timber;

public class DownloadFragment extends BaseFragment{

    private ArrayAdapter<Journal> mAdapter;
    private ArrayList<Journal> mJournalItems = new ArrayList<>();

    public DownloadFragment() { }


    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ListView listView = (ListView) inflater.inflate(R.layout.list_view, container, false);

        // populate the adapter, bind to the listview & set the click listener
        mAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, mJournalItems);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Journal journal = (Journal) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), journal.toString().substring(0, 32), Toast.LENGTH_SHORT).show();
            }
        });
        return listView;
    }


    @Subscribe
    public void getResultsOfSearchQuery(QueryEvent event) {
        // add new results to the adapter to be displayed
        mAdapter.addAll(event.getResultQuery().getResultList().getResult());
    }

    // populate the arraylist on device rotation
    public void setModelDataSet(ArrayList<Journal> list) {
        mJournalItems = list;
    }

}
