package com.example.downloaderdemo.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.adapter.CustomItemDecoration;
import com.example.downloaderdemo.adapter.ListItemAdapter;
import com.example.downloaderdemo.event.QueryEvent;
import com.example.downloaderdemo.model.Article;
import com.example.downloaderdemo.util.QueryPreferences;
import com.example.downloaderdemo.util.QuerySuggestionProvider;
import com.example.downloaderdemo.util.Utils;

import java.util.ArrayList;

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
 * SQLiteDatabase
 * [6] The Busy Coder's Guide to Android Development (https://commonsware.com/Android) p598-608
 *
 * SingleChoiceMode
 * [7] Udacity Advanced Android Development
 *
 */

public class ArticleListFragment extends BaseFragment{

    private static final String SAVED_LOADING = "loading";
    private static final String SAVED_QUERY = "query";
    private static final String SAVED_FIRST_TIME_IN = "firstTimeIn";

    private static SearchRecentSuggestions sRecentSuggestions;
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;
    private ListItemAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private LinearLayoutManager mLayoutManager;
    private String mQuery;
    private boolean mFirstTimeIn = true;
    private View mView;
    private int mChoiceMode;
    private boolean mIsDualPane;
    //private boolean mInitialView = true;

    // endless scrolling variables
    private boolean mLoading = true;
    private int mPreviousTotal, mVisibleThreshold = 5, mFirstVisibleItem, mVisibleItemCount, mTotalItemCount;

    public ArticleListFragment() { }

    public static ArticleListFragment newInstance() {
        return new ArticleListFragment();
    }

    public void setModelDataSet(ArrayList<Article> list) {
        mAdapter.clearAll();
        mAdapter.addAll(list);
        mAdapter.notifyDataSetChanged();
        updateUI();
    }

    // method only called if a fragment is inflated from xml
    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ListItemFragment, 0, 0);
        mChoiceMode = typedArray.getInt(R.styleable.ListItemFragment_android_choiceMode, AbsListView.CHOICE_MODE_NONE);
        boolean autoSelectView = typedArray.getBoolean(R.styleable.ListItemFragment_autoSelectView, false);
        typedArray.recycle();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.recycler_view, container, false);
        mEmptyView = (TextView) mView.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new CustomItemDecoration(getResources().getDimensionPixelSize(R.dimen.dimen_list_item_spacer)));
        mAdapter = new ListItemAdapter(getActivity(), new ArrayList<Article>(), mChoiceMode);

        if(isAdded())
            mRecyclerView.setAdapter(mAdapter);

        // retrieve saved query, if there is one
        mQuery = QueryPreferences.getSavedPrefValue(getActivity(), QueryPreferences.QUERY_STRING);

        if(savedInstanceState == null) {
            mLoading = false;
        } else {
            // re-set fragment state
            mLoading = savedInstanceState.getBoolean(SAVED_LOADING);
            mQuery = savedInstanceState.getString(SAVED_QUERY);
            mFirstTimeIn = savedInstanceState.getBoolean(SAVED_FIRST_TIME_IN);
            mAdapter.onRestoreInstanceState(savedInstanceState);
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
                    executeSearchQuery(); // download more results
                }
            }
        });

        return mView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_LOADING, mLoading);
        outState.putString(SAVED_QUERY, mQuery);
        outState.putBoolean(SAVED_FIRST_TIME_IN, mFirstTimeIn);
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

                    executeSearchQuery();

                } else {
                    Utils.showSnackbar(mView, "Enter a search query");
                }

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
                //dialog.show(getFragmentManager(), "Clear history");
                dialog.show(getFragmentManager(), "Clear history");
            } else {
                Utils.showSnackbar(mView, "History clear");
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void executeSearchQuery() {
        if(Utils.isClientConnected(getActivity())) {
            if(mQuery != null) {
                // post a query, which will be executed by the thread
                postToAppBus(new QueryEvent(mQuery));
                if(mAdapter.getItemCount() == 0) {
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


    private void updateUI() {

        if(mAdapter.getItemCount() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
            if(mFirstTimeIn) {
                Utils.showSnackbar(mView, String.format("%d records returned from the database", mAdapter.getItemCount()));
            }
        }
        else {
            // no records to show
            mEmptyView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            if(mFirstTimeIn) {
                Utils.showSnackbar(mView, "No results available in the database");
            }
        }
        mFirstTimeIn = false;
    }


    public void isDualPane(boolean value) {
        mIsDualPane = value;
        // set the initial view of the detail pane to be that of the first item in the list
        if(mIsDualPane) {
            mAdapter.setInitialView(0);
        }
    }


    // use DialogFragment from the support lib since the fragment is also from the support library
    public static class ConfirmationDialogFragment extends android.support.v4.app.DialogFragment {

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
