package com.example.downloaderdemo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.event.QueryEvent;
import com.example.downloaderdemo.model.Journal;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

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
        //mAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, mJournalItems);
        mAdapter = new CustomArrayAdapter(mJournalItems);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Journal journal = (Journal) parent.getItemAtPosition(position);
                // FIXME throws NPE if title less than 24 chars
                Toast.makeText(getActivity(), journal.toString().substring(0, 24), Toast.LENGTH_SHORT).show();
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


    private class CustomArrayAdapter extends ArrayAdapter<Journal> {

        public CustomArrayAdapter(ArrayList<Journal> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // instantiate the list item if req'd
            if(convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_item, null);
            }

            // configure the view
            Journal journal = getItem(position);

            TextView articleTitle = (TextView) convertView.findViewById(R.id.article_title);
            TextView journalTitle = (TextView) convertView.findViewById(R.id.journal_title);
            TextView articleAuthors = (TextView) convertView.findViewById(R.id.article_authors);
            TextView pageInformation = (TextView) convertView.findViewById(R.id.page_information);
            TextView journalIssue = (TextView) convertView.findViewById(R.id.journal_issue);
            TextView journalVolume = (TextView) convertView.findViewById(R.id.journal_volume);
            TextView publicationYear = (TextView) convertView.findViewById(R.id.publication_year);

            articleTitle.setText(journal.getTitle());
            journalTitle.setText(journal.getJournalTitle());
            articleAuthors.setText(journal.getAuthorString());
            pageInformation.setText(journal.getPageInfo());
            journalIssue.setText(journal.getJournalIssn());
            journalVolume.setText(journal.getJournalVolume());
            publicationYear.setText(journal.getPubType());

            return convertView;
        }


    }


    // TODO create custom ViewHolder


}
