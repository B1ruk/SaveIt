package io.start.biruk.saveit.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.presenter.StatusPresenter;
import io.start.biruk.saveit.view.statusView.StatusView;

/**
 * Created by biruk on 02/10/18.
 */

public class StatusDialog extends DialogFragment implements StatusView{
    private static final String TAG="StatusDialog";

    @Inject StatusPresenter statusPresenter;

    @Bind(R.id.status_article_count) TextView articleCounterView;
    @Bind(R.id.status_total_size) TextView totalSizeView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view= LayoutInflater.from(getContext()).inflate(R.layout.status_dialog_layout,null);
        ButterKnife.bind(this,view);

        return new AlertDialog.Builder(getContext())
                .setTitle("Status")
                .setView(view)
                .setPositiveButton(android.R.string.ok,null)
                .create();
    }

    @Override
    public void onStart() {
        super.onStart();
        App.getAppComponent().inject(this);

        statusPresenter.attachView(this);

        statusPresenter.loadArticlesCount();
        statusPresenter.loadArticlesTotalSize();
    }

    @Override
    public void displayTotalSize(String msg) {
        totalSizeView.setText(msg);
    }

    @Override
    public void displayArticleCount(String msg) {
     articleCounterView.setText(msg);
    }

    @Override
    public void displayError(String msg, Throwable e) {
        Log.e(TAG,msg,e);
    }

    @Override
    public void onDestroyView() {
        statusPresenter.detachView();
        super.onDestroyView();
    }
}
