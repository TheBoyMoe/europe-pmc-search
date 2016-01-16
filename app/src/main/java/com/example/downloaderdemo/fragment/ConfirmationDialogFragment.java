package com.example.downloaderdemo.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;

import com.example.downloaderdemo.R;
import com.example.downloaderdemo.util.Utils;

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
                               Utils.showSnackbar(getActivity().findViewById(R.id.coordinator_layout), "Search history cleared");
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
