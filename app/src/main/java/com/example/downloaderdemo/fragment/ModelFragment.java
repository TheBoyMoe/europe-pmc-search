package com.example.downloaderdemo.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.downloaderdemo.EuroPMCApplication;
import com.example.downloaderdemo.event.QueryEvent;
import com.example.downloaderdemo.event.ResultQueryEvent;
import com.example.downloaderdemo.model.Journal;
import com.example.downloaderdemo.model.ResultQuery;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Headless fragment which maintains the state of the app
 */
public class ModelFragment extends BaseFragment{

    private boolean mIsStarted = false;
    private List<Journal> mJournals = new ArrayList<>();
    private String mQuery;
    private String mCurrentPage;

    public ModelFragment() {}

    public static ModelFragment newInstance() {
        return new ModelFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // headless fragment returns no view
        return null;
    }


    @Subscribe
    public void getSearchQuery(QueryEvent event) {

        if(mQuery != null && event.getQuery() != null) {
            if(!mQuery.equals(event.getQuery())) {
                // new query is received
                mJournals.clear();
            }
        }
        mQuery = event.getQuery();
        mCurrentPage = String.valueOf(event.getPageNumber());
        Timber.i("Received event: query: %s, current page: %s, cache size %d",
                            mQuery, mCurrentPage, mJournals.size());

        if(mQuery != null && !mQuery.isEmpty()) {
            // execute the background thread
            if(!mIsStarted) {
                mIsStarted = true;
                new DownloaderThread().start();
            }
        }
    }


    public ArrayList<Journal> getModel() {
        return new ArrayList<>(mJournals);
    }


    class DownloaderThread extends Thread {

        public DownloaderThread() { }

        @Override
        public void run() {
            if(!isInterrupted()) {

                // uri parameters
                String format = "json"; // json, xml, dc
                String dataset = "fulltext";
                String resultType = "core"; // returns full meta-data for the journal
                String pageSize = "12"; // no. of records returned
                //String page = "1"; // first page
                //String query = "anaemia";

                // uri constants
                final String SEARCH_BASE_URL =
                        "http://www.ebi.ac.uk/europepmc/webservices/rest/search";
                final String QUERY_PARAM = "query";
                final String FORMAT_PARAM = "format";
                final String RESULT_TYPE_PARAM = "resultType";
                final String DATA_SET_PARAM = "dataset";
                final String PAGE_PARAM = "page";
                final String PAGE_SIZE = "pageSize";

                Uri uri = Uri.parse(SEARCH_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, mQuery)
                        .appendQueryParameter(PAGE_PARAM, mCurrentPage)
                        .appendQueryParameter(PAGE_SIZE, pageSize)
                        .appendQueryParameter(RESULT_TYPE_PARAM, resultType)
                        .appendQueryParameter(DATA_SET_PARAM, dataset)
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .build();

                HttpURLConnection con = null;

                try {
                    URL url = new URL(uri.toString());
                    Timber.i("Url: %s", url);
                    con = (HttpURLConnection) url.openConnection();
                    InputStream is = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    ResultQuery resultQuery = new Gson().fromJson(reader, ResultQuery.class);
                    EuroPMCApplication.postToBus(new ResultQueryEvent(resultQuery)); // post new results

                    // add the results to the current journal list
                    mJournals.addAll(resultQuery.getResultList().getResult()); // add to the cache

                    reader.close();
                    mIsStarted = false;

                } catch (MalformedURLException e) {
                    Timber.e("Failure building the url: %s", e.getMessage());
                } catch (IOException e) {
                    Timber.e("Failure to open connection: %s", e.getMessage());
                } finally {
                    if(con != null)
                        con.disconnect();
                }

            }
        }
    }

}
