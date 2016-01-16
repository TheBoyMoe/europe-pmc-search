package com.example.downloaderdemo.util;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Utils {

    private Utils() {
        throw new AssertionError();
    }

    // hide the keyboard on executing search
    public static void hideKeyboard(Activity activity, IBinder windowToken) {
        InputMethodManager mgr =
                (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }



}
