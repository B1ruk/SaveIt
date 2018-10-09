package io.start.biruk.saveit.view.articleView.articleOptions;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.util.FileSizeUtil;
import io.start.biruk.saveit.util.TagStringUtil;

/**
 * Created by biruk on 5/19/2018.
 */
public class ArticleInfoDialog extends DialogFragment {

    private ArticleModel articleModel;
    private static final String ARTICLE_MODEL_DATA = "article_data";
    private static final String TAG = "ArticleInfoDialog";

    @Bind(R.id.article_info_path) TextView pathTextView;
    @Bind(R.id.article_info_tags) TextView tagsView;
    @Bind(R.id.article_info_save_date) TextView articleSavedDateView;
    @Bind(R.id.article_size) TextView fileSizeView;


    public static ArticleInfoDialog newInstance(ArticleModel articleModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARTICLE_MODEL_DATA, articleModel);

        ArticleInfoDialog articleInfoDialog = new ArticleInfoDialog();
        articleInfoDialog.setArguments(bundle);

        return articleInfoDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.articleModel = ((ArticleModel) getArguments().getSerializable(ARTICLE_MODEL_DATA));

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_article_info, null);
        ButterKnife.bind(this, view);

        pathTextView.setText(articleModel.getPath());
        tagsView.setText(TagStringUtil.getFormatedTag(articleModel.getTags()));
        articleSavedDateView.setText(articleModel.getSavedDate());

        FileSizeUtil.computeFolderSizeSingle(articleModel.getPath())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Long size) {
                        fileSizeView.setText(FileSizeUtil.sizeFormater(size));

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });


        return new AlertDialog.Builder(getActivity())
                .setTitle(articleModel.getTitle())
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create();

    }
}
