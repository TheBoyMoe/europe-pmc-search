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
import com.example.downloaderdemo.event.ResultQueryEvent;
import com.example.downloaderdemo.model.Article;
import com.example.downloaderdemo.util.HorizontalDivider;
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
 */

public class DownloadFragment extends BaseFragment{

    private static final String SAVED_CURRENT_PAGE = "current page";
    private static final String SAVED_LOADING = "loading";
    private static final String SAVED_QUERY = "query";

    private ArrayList<Article> mArticleItems = new ArrayList<>();
    private static SearchRecentSuggestions sRecentSuggestions;
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;
    private JournalAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private LinearLayoutManager mLayoutManager;
    private String mQuery;
    private View mView;
    private boolean mNewQuerySubmitted;

    // endless scrolling variables
    private boolean mLoading = true;
    private int mPreviousTotal, mVisibleThreshold = 5, mFirstVisibleItem, mVisibleItemCount,
                    mTotalItemCount, mCurrentPage;

    public DownloadFragment() { }

    public static DownloadFragment newInstance() {
        return new DownloadFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        mView = inflater.inflate(R.layout.recycler_view, container, false);
        mEmptyView = (TextView) mView.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        Drawable itemDivider = ContextCompat.getDrawable(getActivity(), R.drawable.item_divider);
        mRecyclerView.addItemDecoration(new HorizontalDivider(itemDivider));

        mAdapter = new JournalAdapter(mArticleItems);
        mRecyclerView.setAdapter(mAdapter);

        if(savedInstanceState == null) {
            mCurrentPage = 1;
            mLoading = false;
            // execute the last saved query
            mQuery = QueryPreferences.getSavedQueryString(getActivity());
            if(mQuery != null) {
                getSearchResults(); // execute search in background thread
            } else {
                Utils.showSnackbar(getActivity().findViewById(R.id.coordinator_layout), "Enter a search query");
            }
        } else {
            // re-set fragment state
            mCurrentPage = savedInstanceState.getInt(SAVED_CURRENT_PAGE);
            mLoading = savedInstanceState.getBoolean(SAVED_LOADING);
            mQuery = savedInstanceState.getString(SAVED_QUERY);
        }

        if(mQuery == null || mArticleItems.size() == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }


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
                    //postToAppBus(new QueryEvent(mQuery, mCurrentPage));
                }
            }
        });

        return mView;
    }

    private void getSearchResults() {
        if(Utils.isClientConnected(getActivity())) {
            postToAppBus(new QueryEvent(mQuery, mCurrentPage));
        } else {
            Utils.showSnackbar(getActivity().findViewById(R.id.coordinator_layout), "No network connection");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_CURRENT_PAGE, mCurrentPage);
        outState.putBoolean(SAVED_LOADING, mLoading);
        outState.putString(SAVED_QUERY, mQuery);
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
                if (mQuery != null && !mQuery.equals(query)) {
                    // new query has been submitted
                    mNewQuerySubmitted = true;
                }
                mQuery = query;

                // reset the page number
                mCurrentPage = 1;

                // hide the soft keyboard
                Utils.hideKeyboard(getActivity(), mSearchView.getWindowToken());

                // close the search view
                mSearchMenuItem.collapseActionView();

                // save the search query to the RecentSuggestionsProvider
                sRecentSuggestions = new SearchRecentSuggestions(getActivity(),
                        QuerySuggestionProvider.AUTHORITY, QuerySuggestionProvider.MODE);
                sRecentSuggestions.saveRecentQuery(mQuery, null);

                // post the query submitted by the user to the bus and save it to shared prefs
                //postToAppBus(new QueryEvent(mQuery, mCurrentPage));
                getSearchResults();
                QueryPreferences.setSavedQueryString(getActivity(), mQuery);

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

            // clear the saved query from shared prefs
            QueryPreferences.setSavedQueryString(getActivity(), null);
            Timber.i("Cleared shared prefs");

            // clear user's search history
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

    @Subscribe
    public void getResultsOfSearchQuery(ResultQueryEvent event) {
        // clear the arraylist if a new query has been submitted
        if(mNewQuerySubmitted) {
            mNewQuerySubmitted = false;
            mArticleItems.clear();
            mPreviousTotal = 0;
        }
        List<Article> resultList = event.getResultQuery().getResultList().getResult();
        if(resultList.size() > 0) {
            // add new results to the adapter
            mArticleItems.addAll(resultList);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            if(mCurrentPage > 1) {
                Utils.showSnackbar(mView, "Downloading more items");
            }
            ++mCurrentPage;
        } else {
            // no results returned
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
            Utils.showSnackbar(mView, "No results found");
        }

    }


    // populate the arraylist on device rotation
    public void setModelDataSet(ArrayList<Article> list) {
        mArticleItems = list;
    }


    private class JournalAdapter extends RecyclerView.Adapter<JournalViewHolder> {

        private List<Article> mArticles;

        public JournalAdapter(List<Article> items) {
            mArticles = items;
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
            Article article = mArticles.get(position);
            holder.bindJournal(article);
        }

        @Override
        public int getItemCount() {
            return mArticles.size();
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


    private class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Article mArticle;

        TextView articleTitle = null;
        TextView journalTitle = null;
        TextView articleAuthors = null;
        TextView pageInformation = null;
        TextView journalIssue = null;
        TextView journalVolume = null;
        TextView yearOfPublication = null;
        //TextView cited = null;

        public JournalViewHolder(View view) {

            super(view);
            view.setOnClickListener(this);

            articleTitle = (TextView) view.findViewById(R.id.article_title);
            journalTitle = (TextView) view.findViewById(R.id.journal_title);
            articleAuthors = (TextView) view.findViewById(R.id.article_authors);
            pageInformation = (TextView) view.findViewById(R.id.page_information);
            journalIssue = (TextView) view.findViewById(R.id.journal_issue);
            journalVolume = (TextView) view.findViewById(R.id.journal_volume);
            yearOfPublication = (TextView) view.findViewById(R.id.publication_year);
            //cited = (TextView) view.findViewById(R.id.cited_times);
        }


        public void bindJournal(Article article) {
            mArticle = article;
            articleTitle.setText(validateStringValue(mArticle.getTitle()));
            articleAuthors.setText(validateStringValue(mArticle.getAuthorString()));
            pageInformation.setText(validateStringValue(mArticle.getPageInfo()));
             //cited.setText(validateLongValue(mArticle.getCitedByCount()));

            if(mArticle.getJournalInfo() != null) {
                yearOfPublication.setText(validateLongValue(mArticle.getJournalInfo().getYearOfPublication()));
                journalVolume.setText(validateStringValue(mArticle.getJournalInfo().getVolume()));
                journalIssue.setText(validateStringValue(mArticle.getJournalInfo().getIssue()));

                if(mArticle.getJournalInfo().getJournal() != null) {
                    journalTitle.setText(validateStringValue(mArticle.getJournalInfo().getJournal().getTitle()));
                } else {
                    journalTitle.setText(R.string.na_label);
                }
            } else {
                yearOfPublication.setText(R.string.na_label);
                journalVolume.setText(R.string.na_label);
                journalIssue.setText(R.string.na_label);
                journalTitle.setText(R.string.na_label);
            }
        }

        @Override
        public void onClick(View view) {
            // post on click event to the bus
            postToAppBus(new OnListItemClickEvent(mArticle));
        }


        private String validateStringValue(String value) {
            if(value != null && !value.equals("")){
                return value;
            } else {
                return getString(R.string.na_label);
            }
        }

        private String validateLongValue(Long value) {
            if(value != null)
                return String.valueOf(value);
            else
                return getString(R.string.na_label);
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
                                    Utils.showSnackbar(getActivity().findViewById(R.id.coordinator_layout), "Search history cleared");
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
