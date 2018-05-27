package io.start.biruk.saveit.view.articleView.articleOptions;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/19/2018.
 */
public  class BaseArticleDialog extends DialogFragment {
    protected ArticleModel articleModel;
    private static final String ARTICLE_MODEL_DATA = "article_data";


    public static BaseArticleDialog newInstance(ArticleModel articleModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARTICLE_MODEL_DATA, articleModel);

        BaseArticleDialog baseInfoDialog = new BaseArticleDialog();
        baseInfoDialog.setArguments(bundle);

        return baseInfoDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.articleModel = ((ArticleModel) getArguments().getSerializable(ARTICLE_MODEL_DATA));
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        EditText titleEditText=new EditText(getActivity());
        titleEditText.setText(articleModel.getTitle());

        return new AlertDialog.Builder(getActivity())
                .setTitle("Rename title to")
                .setView(titleEditText)
                .setNegativeButton(android.R.string.cancel,null)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String titleEdtTxt = titleEditText.getText().toString();
                    if (!titleEdtTxt.equals(articleModel.getTitle())){
                        Log.d(getClass().getSimpleName(),titleEdtTxt+" edited !!!!!!!!!!");
                    }
                })
                .create();
    }
}
