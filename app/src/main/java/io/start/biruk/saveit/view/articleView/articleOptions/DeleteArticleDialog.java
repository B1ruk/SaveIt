package io.start.biruk.saveit.view.articleView.articleOptions;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/19/2018.
 */
public class DeleteArticleDialog extends DialogFragment {

    private ArticleModel articleModel;
    private static final String ARTICLE_MODEL_DATA = "article_data";


    public static DeleteArticleDialog newInstance(ArticleModel articleModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARTICLE_MODEL_DATA, articleModel);

        DeleteArticleDialog deleteInfoDialog = new DeleteArticleDialog();
        deleteInfoDialog.setArguments(bundle);

        return deleteInfoDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.articleModel = ((ArticleModel) getArguments().getSerializable(ARTICLE_MODEL_DATA));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LinearLayout linearLayout=new LinearLayout(getActivity());

        TextView dltTextView=new TextView(getActivity());
        linearLayout.addView(dltTextView);

        dltTextView.setText("Are you sure you want to delete.\n\t"+articleModel.getTitle());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setView(linearLayout)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {

                })
                .create();
    }


}
