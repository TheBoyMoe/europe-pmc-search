package com.example.downloaderdemo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.event.QueryEvent;
import com.example.downloaderdemo.model.Journal;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class DownloadFragment extends BaseFragment{

    private ArrayList<Journal> mJournalItems = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private JournalAdapter mAdapter;

    public DownloadFragment() { }


    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new JournalAdapter(mJournalItems);
        mRecyclerView.setAdapter(mAdapter);

        return mRecyclerView;
    }


    @Subscribe
    public void getResultsOfSearchQuery(QueryEvent event) {
        // add new results to the adapter
        mJournalItems.addAll(event.getResultQuery().getResultList().getResult());
        mAdapter.notifyDataSetChanged();
    }

    // populate the arraylist on device rotation
    public void setModelDataSet(ArrayList<Journal> list) {
        mJournalItems = list;
    }


    private class JournalAdapter extends RecyclerView.Adapter<JournalViewHolder> {

        private List<Journal> mJournals;

        public JournalAdapter(List<Journal> items) {
            mJournals = items;
        }


        @Override
        public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // create the viewholder
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item, parent, false);

            return new JournalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(JournalViewHolder holder, int position) {

            // populate the viewholder
            Journal journal = mJournals.get(position);
            holder.bindJournal(journal);
        }

        @Override
        public int getItemCount() {
            return mJournals.size();
        }
    }



    private class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Journal mJournal;

        TextView articleTitle = null;
        TextView journalTitle = null;
        TextView articleAuthors = null;
        TextView pageInformation = null;
        TextView journalIssue = null;
        TextView journalVolume = null;
        TextView publicationYear = null;

        public JournalViewHolder(View view) {

            super(view);
            view.setOnClickListener(this);

            articleTitle = (TextView) view.findViewById(R.id.article_title);
            journalTitle = (TextView) view.findViewById(R.id.journal_title);
            articleAuthors = (TextView) view.findViewById(R.id.article_authors);
            pageInformation = (TextView) view.findViewById(R.id.page_information);
            journalIssue = (TextView) view.findViewById(R.id.journal_issue);
            journalVolume = (TextView) view.findViewById(R.id.journal_volume);
            publicationYear = (TextView) view.findViewById(R.id.publication_year);
        }


        public void bindJournal(Journal journal) {
            mJournal = journal;
            articleTitle.setText(mJournal.getTitle());
            journalTitle.setText(mJournal.getJournalTitle());
            articleAuthors.setText(mJournal.getAuthorString());
            pageInformation.setText(mJournal.getPageInfo());
            journalIssue.setText(mJournal.getJournalIssn());
            journalVolume.setText(mJournal.getJournalVolume());
            publicationYear.setText(mJournal.getPubYear());

        }

        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), articleTitle.getText().toString().substring(0, 24), Toast.LENGTH_SHORT).show();
        }


    }



}
