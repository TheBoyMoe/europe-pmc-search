package com.example.downloaderdemo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.event.QueryEvent;
import com.example.downloaderdemo.event.ResultQueryEvent;
import com.example.downloaderdemo.model.Journal;
import com.example.downloaderdemo.util.QueryPreferences;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * References:
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch25s02.html (SearchView setup)
 * [2] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch25s03.html (Save query to shared prefs)
 */
public class DownloadFragment extends BaseFragment{

    private ArrayList<Journal> mJournalItems = new ArrayList<>();
    private JournalAdapter mAdapter;

    public DownloadFragment() { }


    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(savedInstanceState == null) {
            // execute the last saved query
            String query = QueryPreferences.getSavedQueryString(getActivity());
            Timber.i("First time in, retrieving query: %s from shared prefs", query);
            if(query != null) {
                postToAppBus(new QueryEvent(query));
            } else {
                Toast.makeText(getActivity(), "Enter a search query", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_view, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new JournalAdapter(mJournalItems);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        // configure search view
        MenuItem searchItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // post the query submitted by the user to the bus and save it to shared prefs
                postToAppBus(new QueryEvent(query));
                QueryPreferences.setSavedQueryString(getActivity(), query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.clear_search) {
            // clear the saved query from shared prefs
            QueryPreferences.setSavedQueryString(getActivity(), null);
            Timber.i("Cleared shared prefs");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void getResultsOfSearchQuery(ResultQueryEvent event) {
        // add new results to the adapter
        mJournalItems.clear();
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

        public void clearAll() {
            mJournalItems.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<Journal> items) {
            mJournalItems.addAll(items);
            notifyDataSetChanged();
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
