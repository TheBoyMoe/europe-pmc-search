package com.example.downloaderdemo.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.event.OnListItemClickEvent;
import com.example.downloaderdemo.event.QueryEvent;
import com.example.downloaderdemo.event.RefreshAdapterEvent;
import com.example.downloaderdemo.model.Article;
import com.example.downloaderdemo.network.DownloaderThread;
import com.example.downloaderdemo.ui.ChoiceCapableAdapter;
import com.example.downloaderdemo.ui.HorizontalDivider;
import com.example.downloaderdemo.ui.SingleChoiceMode;
import com.example.downloaderdemo.util.QueryPreferences;
import com.example.downloaderdemo.util.QuerySuggestionProvider;
import com.example.downloaderdemo.util.Utils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * References:
 *
 * RecyclerView
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch25s02.html (SearchView setup)
 * [2] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch25s03.html (Save query to shared prefs)
 *
 * SearchView
 * [3] https://developer.android.com/training/search/setup.html
 * [4] http://antonioleiva.com/actionbarcompat-action-views/
 * [5] http://developer.android.com/guide/topics/search/adding-recent-query-suggestions.html
 *
 * Endless scrolling on a RecyclerView
 * [4] http://stackoverflow.com/questions/26543131/how-to-implement-endless-list-with-recyclerview
 * [5] http://androhub.com/load-more-items-on-scroll-android/
 *
 * SingleChoiceMode
 * [6] The Busy Coder's Guide to Android Development (https://commonsware.com/Android) p1260
 *
 * SQLiteDatabase
 * [7] The Busy Coder's Guide to Android Development (https://commonsware.com/Android) p598-608
 *
 */

public class ArticleListFragment extends BaseFragment{

    private static final String SAVED_LOADING = "loading";
    private static final String SAVED_QUERY = "query";

    private ArrayList<Article> mArticleItems = new ArrayList<>();
    private static SearchRecentSuggestions sRecentSuggestions;
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;
    private ChoiceCapableAdapter<?> mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private LinearLayoutManager mLayoutManager;
    private String mQuery;
    private boolean mFirstTimeIn;
    private View mView;


    // endless scrolling variables
    private boolean mLoading = true;
    private int mPreviousTotal, mVisibleThreshold = 5, mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    public ArticleListFragment() { }

    public static ArticleListFragment newInstance() {
        return new ArticleListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null) {
            mAdapter.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.recycler_view, container, false);
        mEmptyView = (TextView) mView.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        Drawable itemDivider = ContextCompat.getDrawable(getActivity(), R.drawable.item_divider);
        mRecyclerView.addItemDecoration(new HorizontalDivider(itemDivider));

        mAdapter = new JournalAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        // retrieve saved query, if there is one
        mQuery = QueryPreferences.getSavedPrefValue(getActivity(), QueryPreferences.QUERY_STRING);

        if(savedInstanceState == null) {
            mFirstTimeIn = true;
            mLoading = false;

        } else {
            // re-set fragment state
            mLoading = savedInstanceState.getBoolean(SAVED_LOADING);
            mQuery = savedInstanceState.getString(SAVED_QUERY);
        }

        // refresh the UI with any results returned from the dbase
        updateUI();

        // implement endless scrolling
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mVisibleItemCount = recyclerView.getChildCount();
                mTotalItemCount = mLayoutManager.getItemCount();
                mFirstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if(mLoading) {
                    if(mTotalItemCount > mPreviousTotal) {
                        mLoading = false;
                        mPreviousTotal = mTotalItemCount;
                    }
                }
                if(!mLoading && (mTotalItemCount - mVisibleItemCount)
                        <= (mFirstVisibleItem  + mVisibleThreshold)) {
                    // end of page has been reached, download more items
                    mLoading = true;
                    getSearchResults();
                }
            }
        });

        return mView;
    }


    private void getSearchResults() {
        if(Utils.isClientConnected(getActivity())) {
            if(mQuery != null) {
                postToAppBus(new QueryEvent(mQuery));
                Timber.i("Posting query: %s to bus, ARTICLE SIZE: %d", mQuery, mArticleItems.size());
                if(mArticleItems.size() == 0) {
                    mEmptyView.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                }


            } else {
                Utils.showSnackbar(mView, "No more results, enter a query");
            }
        } else {
            Utils.showSnackbar(getActivity().findViewById(R.id.coordinator_layout), "No network connection");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_LOADING, mLoading);
        outState.putString(SAVED_QUERY, mQuery);
        mAdapter.onSaveInstanceState(outState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search, menu);

        // configure search view
        SearchManager mgr = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        mSearchMenuItem = menu.findItem(R.id.search_view);
        mSearchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchView.setSearchableInfo(mgr.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryRefinementEnabled(true);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (query != null && !query.isEmpty()) {
                    mQuery = query;

                    // save the search query to the RecentSuggestionsProvider
                    sRecentSuggestions = new SearchRecentSuggestions(getActivity(),
                            QuerySuggestionProvider.AUTHORITY, QuerySuggestionProvider.MODE);
                    sRecentSuggestions.saveRecentQuery(mQuery, null);

                    getSearchResults();

                } else {
                    Utils.showSnackbar(mView, "Enter a search query");
                }

                // reset firstTimeIn flag
                mFirstTimeIn = false;

                // hide the soft keyboard
                Utils.hideKeyboard(getActivity(), mSearchView.getWindowToken());

                // close the search view
                mSearchMenuItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // no-op
                return false;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.clear_search) {

            // clear user's search history & shared prefs
            if(sRecentSuggestions != null) {
                ConfirmationDialogFragment dialog = new ConfirmationDialogFragment();
                dialog.show(getFragmentManager(), "Clear history");
            } else {
                Utils.showSnackbar(mView, "History clear");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setModelDataSet(ArrayList<Article> list) {
        Timber.i("setDataModelSet() called, size: %d", list.size());
        mArticleItems = list;
    }


    @Subscribe
    public void hasAdapterBeenRefreshed(RefreshAdapterEvent event) {
        if(event.isRefreshAdapter()) {
            updateUI();
            if(mFirstTimeIn) {
                mFirstTimeIn = false;
                if(mArticleItems.size() > 0)
                    Utils.showSnackbar(mView, "Results returned from database");
                else
                    Utils.showSnackbar(mView, "No results saved to the database");
            } else
            if(mArticleItems.size() > Integer.valueOf(DownloaderThread.PAGE_SIZE)) {
                Utils.showSnackbar(getActivity().findViewById(R.id.coordinator_layout), "Downloading more items");
            }
        }
    }

    private void updateUI() {

        if(mArticleItems.size() > 0) {
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            Timber.i("Refreshing UI");
        } else {
            // no records to show
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            Timber.i("Empty data set, nothing to show!");
        }
    }


    private class JournalAdapter extends ChoiceCapableAdapter<JournalViewHolder> {


        public JournalAdapter(RecyclerView recyclerView) {
            super(recyclerView, new SingleChoiceMode());
        }


        @Override
        public JournalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            // create the viewholder
            return (new JournalViewHolder(this, getActivity().getLayoutInflater()
                            .inflate(R.layout.list_item, parent, false)));
        }

        @Override
        public void onBindViewHolder(JournalViewHolder holder, int position) {
            // populate the viewholder
            holder.bindJournal(mArticleItems.get(position));
        }

        @Override
        public int getItemCount() {
            return mArticleItems.size();
        }

        public void clearAll() {
            mArticleItems.clear();
            notifyDataSetChanged();
        }

        public void addAll(List<Article> items) {
            mArticleItems.addAll(items);
            notifyDataSetChanged();
        }

    }


    public class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Article mArticle;
        private ChoiceCapableAdapter mAdapter;
        private View mView;

        TextView articleTitle = null;
        TextView journalTitle = null;
        TextView articleAuthors = null;
        TextView pageInformation = null;
        TextView journalIssue = null;
        TextView journalVolume = null;
        TextView yearOfPublication = null;
        //TextView cited = null;

        public JournalViewHolder(ChoiceCapableAdapter adapter, View view) {
            super(view);
            mAdapter = adapter;
            mView = view;

            articleTitle = (TextView) mView.findViewById(R.id.article_title);
            journalTitle = (TextView) mView.findViewById(R.id.journal_title);
            articleAuthors = (TextView) mView.findViewById(R.id.article_authors);
            pageInformation = (TextView) mView.findViewById(R.id.page_information);
            journalIssue = (TextView) mView.findViewById(R.id.journal_issue);
            journalVolume = (TextView) mView.findViewById(R.id.journal_volume);
            yearOfPublication = (TextView) mView.findViewById(R.id.publication_year);
            //cited = (TextView) view.findViewById(R.id.cited_times);

            mView.setOnClickListener(this);
        }


        public void bindJournal(Article article) {

            // article object read back from the database
            mArticle = article;

            articleTitle.setText(mArticle.getTitle());
            articleAuthors.setText(mArticle.getAuthorString());
            pageInformation.setText(mArticle.getPageInfo());

            if(mArticle.getJournalInfo().getYearOfPublication() == 0) {
                yearOfPublication.setText(getString(R.string.na_label));
            } else {
                yearOfPublication.setText(String.valueOf(mArticle.getJournalInfo().getYearOfPublication()));
            }

            journalVolume.setText(mArticle.getJournalInfo().getVolume());
            journalIssue.setText(mArticle.getJournalInfo().getIssue());
            journalTitle.setText(mArticle.getJournalInfo().getJournal().getTitle());

            setChecked(mAdapter.isChecked(getAdapterPosition()));
        }

        public void setChecked(boolean isChecked) {
            mView.setActivated(isChecked);
        }

        @Override
        public void onClick(View view) {
            // post on click event to the bus
            postToAppBus(new OnListItemClickEvent(mArticle));

            // handle checked status
            boolean isCheckedNow = mAdapter.isChecked(getAdapterPosition());
            mAdapter.onChecked(getAdapterPosition(), !isCheckedNow);
            mView.setActivated(!isCheckedNow);
        }


    }


    public static class ConfirmationDialogFragment extends DialogFragment {

        public ConfirmationDialogFragment() {}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.confirmation_dialog_message)
                    .setPositiveButton(R.string.confirmation_dialog_positive_button,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    sRecentSuggestions.clearHistory();
                                    sRecentSuggestions = null;
                                    QueryPreferences.setSavedPrefValue(getActivity(), QueryPreferences.QUERY_STRING, null);
                                    QueryPreferences.setSavedPrefValue(getActivity(), QueryPreferences.CURRENT_PAGE, null);
                                    Utils.showSnackbar(getActivity().findViewById(R.id.coordinator_layout), "Search history cleared");
                                    Timber.i("Cleared shared prefs & SearchView history");
                                }
                            })
                    .setNegativeButton(R.string.confirmation_dialog_negative_button,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Utils.showSnackbar(getActivity().findViewById(R.id.coordinator_layout), "Action cancelled");
                                }
                            });


            return builder.create();
        }
    }



}
