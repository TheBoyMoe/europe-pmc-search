package com.example.downloaderdemo.util;


import android.content.Context;
import android.preference.PreferenceManager;

/**
 * References
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch25s03.html
 */
public class QueryPreferences {

    private static final String QUERY_STRING = "query_string";

    public static String getSavedQueryString(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(QUERY_STRING, null);
    }

    public static void setSavedQueryString(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(QUERY_STRING, query)
                .apply();
    }

}
