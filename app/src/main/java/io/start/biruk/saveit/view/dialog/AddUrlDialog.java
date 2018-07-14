package io.start.biruk.saveit.view.dialog;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.start.biruk.saveit.events.FetchArticleEvent;
import io.start.biruk.saveit.events.UrlFromClipboardEvent;
import io.start.biruk.saveit.util.HttpUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by biruk on 14/07/18.
 */

public class AddUrlDialog extends DialogFragment {

    private String urlFromClipboard="";

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onCopyUrlFromCliboard(UrlFromClipboardEvent urlFromClipboardEvent) {
        this.urlFromClipboard = urlFromClipboardEvent.getUrl();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        EditText urlText = new EditText(getActivity());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Add url")
                .setView(urlText)
                .setNeutralButton("URL from clipboard", (dialog, which) -> {
                    if (!urlFromClipboard.isEmpty()){
                        urlText.setText(urlFromClipboard);
                    }
                })
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String url = urlText.getText().toString();
                    if (HttpUtil.isValid(url)){
                        addContent(url);
                    }
                })
                .create();
    }

    private void addContent(String url) {
        EventBus.getDefault().post(new FetchArticleEvent(url));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
