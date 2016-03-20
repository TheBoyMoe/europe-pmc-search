package com.example.downloaderdemo.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.downloaderdemo.EuroPMCApplication;
import com.example.downloaderdemo.R;
import com.example.downloaderdemo.event.DataModelUpdateEvent;
import com.example.downloaderdemo.event.QueryEvent;
import com.example.downloaderdemo.event.QueryCompletionEvent;
import com.example.downloaderdemo.model.Article;
import com.example.downloaderdemo.model.Journal;
import com.example.downloaderdemo.model.JournalInfo;
import com.example.downloaderdemo.model.KeywordList;
import com.example.downloaderdemo.network.DownloaderThread;
import com.example.downloaderdemo.util.DatabaseHelper;
import com.example.downloaderdemo.util.QueryPreferences;
import com.example.downloaderdemo.util.Utils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * Headless fragment which maintains the state of the app
 */
public class ModelFragment extends BaseFragment{

    // instance variables
    private boolean mIsRunning = false;
    private List<Article> mArticles = new ArrayList<>(); // temp cache of saved results
    private String mQuery;
    private int mCurrentPage;
    private AsyncTask mTask;
    private DatabaseHelper mHelper;
    private String[] mProjection = {
            DatabaseHelper.ROW_ID,
            DatabaseHelper.ARTICLE_ID,
            DatabaseHelper.ARTICLE_TITLE,
            DatabaseHelper.JOURNAL_TITLE,
            DatabaseHelper.AUTHOR_STRING,
            DatabaseHelper.PAGE_INFO,
            DatabaseHelper.ABSTRACT_TEXT,
            DatabaseHelper.KEYWORD_LIST,
            DatabaseHelper.VOLUME,
            DatabaseHelper.ISSUE,
            DatabaseHelper.YEAR_OF_PUBLICATION,
            DatabaseHelper.CITED
    };

    public ModelFragment() {}

    public static ModelFragment newInstance() {
        return new ModelFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mHelper = DatabaseHelper.getInstance(getActivity());

        // check if any records exist and populate the data model
        mTask = new QueryTask().execute();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // headless fragment returns no view
        return null;
    }


    @Subscribe
    public void getSearchQuery(QueryEvent event) {

        // retrieve the posted query
        String searchQuery = event.getQuery();

        // get the last saved search query & page count
        mQuery = QueryPreferences.getSavedPrefValue(getActivity(), QueryPreferences.QUERY_STRING);
        String page = QueryPreferences.getSavedPrefValue(getActivity(), QueryPreferences.CURRENT_PAGE);
        mCurrentPage = page != null ? Integer.valueOf(page) : 1;
        Timber.i("Retrieved page count: %s, retrieved query: %s", page, mQuery);

        if(mQuery == null || !mQuery.equals(searchQuery)) {
            // if it's the first time thru or a new query
            // delete the currently saved results
            mTask = new DeleteTask().execute();
            mQuery = searchQuery;
            mCurrentPage = 1;
        }

        // execute the search on a background thread, if it's not already running
        if(!mIsRunning) {
            mIsRunning = true;
            new DownloaderThread(mQuery, String.valueOf(mCurrentPage)).start();
            Timber.i("Executing search for: %s, current page: %s", mQuery, mCurrentPage);
        }
        QueryPreferences.setSavedPrefValue(getActivity(), QueryPreferences.QUERY_STRING, mQuery);
    }


    // get the results of the search query
    @Subscribe
    public void getResultOfQueryEvent(QueryCompletionEvent event)  {

        List<Article> resultList = event.getResultQuery().getResultList().getResult();
        if(resultList.size() > 0) {

            // add the results to the dbase - automatically executes a query which updates the UI
            Article[] list = resultList.toArray(new Article[resultList.size()]);
            mTask = new InsertTask().execute(list);

            // update the page number & thread status
            ++mCurrentPage;
            mIsRunning = false;

        } else if(resultList.size() == 0 && mQuery != null) {
            Utils.showSnackbar(getActivity().findViewById(R.id.coordinator_layout), "No results found");
        }
        Timber.i("Next page count: %d", mCurrentPage);
        QueryPreferences.setSavedPrefValue(getActivity(),
                QueryPreferences.CURRENT_PAGE, String.valueOf(mCurrentPage));
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mTask != null) {
            // cancel any tasks that have not completed
            mTask.cancel(false);
        }
        // close dbase connection
        mHelper.close();
    }


    // retrieve the datacache
    public ArrayList<Article> getModel() {
        return new ArrayList<>(mArticles);
    }


    public int getSize() {
        return mArticles.size();
    }


    public Article getJournal(int position) {
        return mArticles.get(position);
    }


    // CRUD operations performed by an AsyncTask
    private abstract class BaseTask<T> extends AsyncTask<T, Void, Cursor> {

        @Override
        protected void onPostExecute(Cursor cursor) {

            // convert the cursor to an arraylist of Article Pojos & update the datacache
            Article article;
            JournalInfo journalInfo;
            Journal journal;
            KeywordList keywordList;
            mArticles.clear();
            while(cursor.moveToNext()) {

                article = new Article();
                journalInfo = new JournalInfo();
                journal = new Journal();
                keywordList = new KeywordList();

                // populate article fields
                article.setRowid(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ROW_ID)));
                article.setId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ARTICLE_ID)));
                article.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ARTICLE_TITLE)));
                article.setAuthorString(cursor.getString(cursor.getColumnIndex(DatabaseHelper.AUTHOR_STRING)));
                article.setPageInfo(cursor.getString(cursor.getColumnIndex(DatabaseHelper.PAGE_INFO)));
                article.setAbstractText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ABSTRACT_TEXT)));
                article.setCitedByCount(Long.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.CITED))));

                // populate journalInfo fields
                journalInfo.setIssue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.ISSUE)));
                journalInfo.setVolume(cursor.getString(cursor.getColumnIndex(DatabaseHelper.VOLUME)));
                journalInfo.setYearOfPublication(Long.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.YEAR_OF_PUBLICATION))));

                // populate Journal field
                journal.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.JOURNAL_TITLE)));

                // populate Keywords List field
                List<String> keywords = null;
                String str = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEYWORD_LIST));
                if(!str.equals(getString(R.string.na_label))) {
                    keywords = Arrays.asList(str.split("\\s*,\\s*"));
                }
                keywordList.setKeyword(keywords);

                // add the objects to article
                journalInfo.setJournal(journal);
                article.setJournalInfo(journalInfo);
                article.setKeywordList(keywordList);

                // add each article to the List of results
                mArticles.add(article);
            }

            Timber.i("Datacache contains %d records", mArticles.size());

            cursor.close();

            // inform any interested parties that the data model has been updated
            EuroPMCApplication.postToBus(new DataModelUpdateEvent(true));

        }

        // execute query in doInBackground() of the insert/query/delete tasks
        // resulting cursor passed to onPostExecute()
        String sortOrder = DatabaseHelper.ROW_ID + " ASC";
        protected Cursor doQuery() {
            Cursor result = mHelper.getReadableDatabase()
                    .query(DatabaseHelper.TABLE_NAME,
                            mProjection,
                            null,
                            null,
                            null,
                            null,
                            sortOrder);
            result.getCount(); // ensure the query is executed
            Timber.i("Querying the database, returned %d rows", result.getCount());
            return result; // cursor processed by onPostExecute()
        }

    }


    // query the database, returning all records based on the projection
    private class QueryTask extends BaseTask<Void> {
        @Override
        protected Cursor doInBackground(Void... params) {
            Timber.i("Executing query task");
            return doQuery();
        }
    }


    // insert record into the database
    private class InsertTask extends BaseTask<Article> {
        @Override
        protected Cursor doInBackground(Article... params) {

            Timber.i("Executing insert task");

            // iterate through the array of articles, inserting each in turn
            ContentValues values = new ContentValues();
            SQLiteDatabase db = mHelper.getWritableDatabase();

            for (Article article : params) {
                values.clear();
                values.put(DatabaseHelper.ARTICLE_ID, validateStringValue(article.getId()));
                values.put(DatabaseHelper.ARTICLE_TITLE, validateStringValue(article.getTitle()));
                values.put(DatabaseHelper.AUTHOR_STRING, validateStringValue(article.getAuthorString()));
                values.put(DatabaseHelper.PAGE_INFO, validateStringValue(article.getPageInfo()));
                values.put(DatabaseHelper.ABSTRACT_TEXT, validateStringValue(article.getAbstractText()));

                if(article.getCitedByCount() != null) {
                    values.put(DatabaseHelper.CITED, article.getCitedByCount());
                } else {
                    values.put(DatabaseHelper.CITED, getString(R.string.no_number));
                }

                if(article.getJournalInfo() != null) {
                    values.put(DatabaseHelper.VOLUME, validateStringValue(article.getJournalInfo().getVolume()));
                    values.put(DatabaseHelper.ISSUE, validateStringValue(article.getJournalInfo().getIssue()));

                    if(article.getJournalInfo().getYearOfPublication() != null) {
                        values.put(DatabaseHelper.YEAR_OF_PUBLICATION, article.getJournalInfo().getYearOfPublication());
                    } else {
                        values.put(DatabaseHelper.YEAR_OF_PUBLICATION, getString(R.string.no_number));
                    }

                    if(article.getJournalInfo().getJournal() != null) {
                        values.put(DatabaseHelper.JOURNAL_TITLE, validateStringValue(article.getJournalInfo().getJournal().getTitle()));
                    } else {
                        values.put(DatabaseHelper.JOURNAL_TITLE, getString(R.string.app_name));
                    }
                } else {
                    values.put(DatabaseHelper.VOLUME, getString(R.string.na_label));
                    values.put(DatabaseHelper.ISSUE, getString(R.string.na_label));
                    values.put(DatabaseHelper.YEAR_OF_PUBLICATION, getString(R.string.no_number));
                    values.put(DatabaseHelper.JOURNAL_TITLE, getString(R.string.na_label));
                }

                if(article.getKeywordList() != null && article.getKeywordList().getKeyword() != null) {
                    values.put(DatabaseHelper.KEYWORD_LIST, validateStringValue(article.getKeywordList().getKeyword().toString()));
                } else {
                    values.put(DatabaseHelper.KEYWORD_LIST, getString(R.string.na_label));
                }

                db.insert(DatabaseHelper.TABLE_NAME, DatabaseHelper.ARTICLE_TITLE, values);
            }
            return doQuery(); // returns a cursor, passed to onPostExecute()
        }
    }


    // delete records from the database
    private class DeleteTask extends BaseTask<Void> {
        @Override
        protected Cursor doInBackground(Void... params) {
            Timber.i("Deleting current records from the table");
            // delete the table
            mHelper.getWritableDatabase().delete(DatabaseHelper.TABLE_NAME, null, null);
            return doQuery();
        }
    }


    // helper methods
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
