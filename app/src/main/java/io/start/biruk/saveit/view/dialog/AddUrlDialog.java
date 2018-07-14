package io.start.biruk.saveit.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

/**
 * Created by biruk on 14/07/18.
 */

public class AddUrlDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        EditText urlText=new EditText(getActivity());
        urlText.setHint("http//........");

        return new AlertDialog.Builder(getActivity())
                .setTitle("Add url")
                .setView(urlText)
                .setNegativeButton("URL from clipboard", (dialog, which) -> copyUrlFromClipboard())
                .setPositiveButton(android.R.string.ok, (dialog, which) -> addContent(urlText))
                .create();
    }

    private void addContent(EditText urlText) {

    }

    private void copyUrlFromClipboard() {

    }
}
