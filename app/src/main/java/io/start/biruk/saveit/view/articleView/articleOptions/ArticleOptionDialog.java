package io.start.biruk.saveit.view.articleView.articleOptions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/17/2018.
 */
public class ArticleOptionDialog extends DialogFragment {
    private static final String ARG_ARTICLE_MODEL = "article_model_data";
    public static final String SELECTED_ARTICLE="io.start.biruk.saveit.view.articleView.articleOptions";
    private ArticleModel articleModel;

    public static ArticleOptionDialog newInstance(ArticleModel articleModel) {
        ArticleOptionDialog articleOptionsFragmentView = new ArticleOptionDialog();
        Bundle args = new Bundle();

        args.putSerializable(ARG_ARTICLE_MODEL, articleModel);
        articleOptionsFragmentView.setArguments(args);

        return articleOptionsFragmentView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.articleModel = (ArticleModel) getArguments().getSerializable(ARG_ARTICLE_MODEL);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        CharSequence[] articleOptions = {"edit title", "edit tag", "delete", "info"};

        return new AlertDialog.Builder(getActivity())
                .setTitle(articleModel.getTitle())
                .setItems(articleOptions,(dialog, which) -> {sendResult(Activity.RESULT_OK,articleOptions[which].toString());})
                .create();
    }

    private void sendResult(int resultCode,String options){
        if (getTargetFragment()==null){
            return;
        }

        Intent intent=new Intent();
        intent.setAction(options);
        intent.putExtra(SELECTED_ARTICLE,articleModel);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}
