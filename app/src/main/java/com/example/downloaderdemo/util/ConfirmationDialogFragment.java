package com.example.downloaderdemo.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;

import com.example.downloaderdemo.R;

public class ConfirmationDialogFragment extends DialogFragment{

    private SearchRecentSuggestions mSearchRecentSuggestions;

    public ConfirmationDialogFragment() {}


    public static ConfirmationDialogFragment newInstance() {
        return new ConfirmationDialogFragment();
    }

    public void addSuggestionsToDialog(SearchRecentSuggestions suggestions) {
        mSearchRecentSuggestions = suggestions;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirmation_dialog_message)
               .setPositiveButton(R.string.confirmation_dialog_positive_button,
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               mSearchRecentSuggestions.clearHistory();
                               mSearchRecentSuggestions = null;
                               Utils.showToast(getActivity(), "Search history cleared");
                           }
                       })
               .setNegativeButton(R.string.confirmation_dialog_negative_button,
                       new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               Utils.showToast(getActivity(), "Action cancelled");
                           }
                       });


        return builder.create();
    }
}
