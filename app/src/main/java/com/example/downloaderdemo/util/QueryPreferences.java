package com.example.downloaderdemo.util;


import android.content.Context;
import android.preference.PreferenceManager;

/**
 * References
 * [1] https://www.safaribooksonline.com/library/view/android-programming-the/9780134171517/ch25s03.html
 */
public class QueryPreferences {

    public static final String QUERY_STRING = "query_string";
    public static final String CURRENT_PAGE = "current_page";

    public static String getSavedPrefValue(Context context, String key) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(key, null);
    }

    public static void setSavedPrefValue(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(key, value)
                .apply();
    }

}
