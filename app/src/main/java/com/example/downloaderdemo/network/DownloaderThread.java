package com.example.downloaderdemo.network;

import android.net.Uri;

import com.example.downloaderdemo.EuroPMCApplication;
import com.example.downloaderdemo.event.IsThreadRunningEvent;
import com.example.downloaderdemo.event.ResultQueryEvent;
import com.example.downloaderdemo.model.ResultQuery;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import timber.log.Timber;

public class DownloaderThread extends Thread{

    // http://www.ebi.ac.uk/europepmc/webservices/rest/search?query=malaria&resulttype=core&format=json&pageSize=5&dataset=fulltext&page=5

    private String mQuery;
    private String mCurrentPage;

    public DownloaderThread(String query, String currentPage) {
        mQuery = query;
        mCurrentPage = currentPage;
    }

    @Override
    public void run() {
        if(!isInterrupted()) {

            // uri parameters
            String format = "json"; // json, xml, dc
            String dataset = "fulltext";
            String resultType = "core"; // returns full meta-data for the journal
            String pageSize = "12"; // no. of records returned

            // uri constants
            final String SEARCH_BASE_URL =
                    "http://www.ebi.ac.uk/europepmc/webservices/rest/search";
            final String QUERY_PARAM = "query";
            final String FORMAT_PARAM = "format";
            final String RESULT_TYPE_PARAM = "resulttype";
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

                // use gson to parse the json and post the result to the bus
                ResultQuery resultQuery = new Gson().fromJson(reader, ResultQuery.class);
                EuroPMCApplication.postToBus(new ResultQueryEvent(resultQuery)); // post results

                // add the results to the current journal list
                //mArticles.addAll(resultQuery.getResultList().getResult()); // add to the cache

                reader.close();

                // Let who ever started the thread that it has finished
                EuroPMCApplication.postToBus(new IsThreadRunningEvent(false));

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
