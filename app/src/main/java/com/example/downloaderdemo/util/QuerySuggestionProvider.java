package com.example.downloaderdemo.util;

import android.content.SearchRecentSuggestionsProvider;

public class QuerySuggestionProvider extends SearchRecentSuggestionsProvider{

    public final static String AUTHORITY = "com.example.downloaderdemo.util.QuerySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public QuerySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }


}
