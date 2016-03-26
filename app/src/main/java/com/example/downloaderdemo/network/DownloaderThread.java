package com.example.downloaderdemo.network;

import android.net.Uri;

import com.example.downloaderdemo.EuroPMCApplication;
import com.example.downloaderdemo.event.MessageEvent;
import com.example.downloaderdemo.event.QueryCompletionEvent;
import com.example.downloaderdemo.model.ResultQuery;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import timber.log.Timber;

public class DownloaderThread extends Thread{

    // no api key req'd
    // http://www.ebi.ac.uk/europepmc/webservices/rest/search?query=malaria&resulttype=core&format=json&pageSize=5&dataset=fulltext&page=5

    private String mQuery;
    private String mCurrentPage;
    public static final String PAGE_SIZE = "20";

    public DownloaderThread(String query, String currentPage) {
        mQuery = query;
        mCurrentPage = currentPage;
    }

    @Override
    public void run() {
        if(!isInterrupted()) {

            Timber.i("Executing download thread");

            // uri parameters
            String format = "json"; // json, xml, dc
            String dataset = "fulltext";
            String resultType = "core"; // returns full meta-data for the journal
            String pageSize = PAGE_SIZE; // no. of records returned

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

            Timber.i("Url: %s", uri.toString());

            try {

                OkHttpClient client = new OkHttpClient();
                client.networkInterceptors().add(new StethoInterceptor());
                Request request = new Request.Builder().url(uri.toString()).build();
                Response response = client.newCall(request).execute();

                if(response.isSuccessful()) {
                    Reader in = response.body().charStream();
                    BufferedReader reader = new BufferedReader(in);

                    // use gson to parse the json and post the result to the bus
                    ResultQuery resultQuery = new Gson().fromJson(reader, ResultQuery.class);
                    EuroPMCApplication.postToBus(new QueryCompletionEvent(resultQuery)); // post results

                    reader.close();

                } else {
                    Timber.e("Http response: %s", response.toString());
                    EuroPMCApplication.postToBus(new MessageEvent(MessageEvent.ERROR_EXECUTING_QUERY));
                }

            } catch (IOException e) {
                Timber.e("exception thrown downloading data: %s", e.getMessage());
            }

        }
    }
}
