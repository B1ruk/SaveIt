package io.start.biruk.saveit.view.articleView.articleOptions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/19/2018.
 */
public  class EditTitleArticleDialog extends DialogFragment {
    protected ArticleModel articleModel;
    public static final String ARTICLE_MODEL_DATA = "article_data";


    public static EditTitleArticleDialog newInstance(ArticleModel articleModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARTICLE_MODEL_DATA, articleModel);

        EditTitleArticleDialog baseInfoDialog = new EditTitleArticleDialog();
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
                        ArticleModel modifiedArticle=new ArticleModel.Builder()
                                .url(articleModel.getUrl())
                                .isFavorite(articleModel.isFavorite())
                                .path(articleModel.getPath())
                                .savedDate(articleModel.getSavedDate())
                                .tags(articleModel.getTags())
                                .title(titleEdtTxt)
                                .build();

                        sendResult(Activity.RESULT_OK,modifiedArticle);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode,ArticleModel articleModel){
        if (getTargetFragment()==null){
            return;
        }
        Intent intent=new Intent();
        intent.putExtra(ARTICLE_MODEL_DATA,articleModel);
        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }

}
