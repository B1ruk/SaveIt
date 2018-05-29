package io.start.biruk.saveit.view.articleView.articleOptions;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.presenter.TagPresenter;
import io.start.biruk.saveit.view.tagsView.TagAdapter;
import io.start.biruk.saveit.view.tagsView.TagView;

/**
 * Created by biruk on 5/28/2018.
 */

public class AddTagDialog extends DialogFragment implements TagView{

    @Inject TagPresenter tagPresenter;
    @Inject Picasso picasso;

    @Bind(R.id.dialog_article_options_tags_recycler_view) RecyclerView tagRecyclerView;
    @Bind(R.id.dialog_article_options_empty_tags_view) ImageView tagEmptyImageView;
    @Bind(R.id.dialog_article_options_empty_text_view) TextView tagEmptyTextView;

    public static final String ARTICLE_MODEL_DATA = "article_data";
    private ArticleModel articleModel;

    public static AddTagDialog newInstance(ArticleModel articleModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARTICLE_MODEL_DATA, articleModel);

        AddTagDialog addTagDialog = new AddTagDialog();
        addTagDialog.setArguments(bundle);

        return addTagDialog;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getAppComponent().inject(this);

        this.articleModel = ((ArticleModel) getArguments().getSerializable(ARTICLE_MODEL_DATA));
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void displayTags(List<String> tags) {
        tagRecyclerView.setVisibility(View.VISIBLE);
        tagEmptyImageView.setVisibility(View.GONE);
        tagEmptyTextView.setVisibility(View.GONE);

        TagAdapter tagAdapter=new TagAdapter(tag -> sendResult(tag,articleModel),tags);

        tagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tagRecyclerView.setAdapter(tagAdapter);
    }

    @Override
    public void displayEmptyTagView() {
        tagEmptyImageView.setVisibility(View.VISIBLE);
        tagEmptyTextView.setVisibility(View.VISIBLE);
        tagRecyclerView.setVisibility(View.GONE);

        picasso.load(R.drawable.ic_tag_file_black_24dp)
                .resize(70,70)
                .into(tagEmptyImageView);

        tagEmptyTextView.setText("no tags found");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_article_tag_option, null);
        ButterKnife.bind(this,view);

        tagPresenter.attachView(this);
        tagPresenter.loadTags();

        return new AlertDialog.Builder(getActivity())
                .setTitle("Add Tag")
                .setView(view)
                .setNegativeButton(android.R.string.cancel,null)
                .setPositiveButton("New Tag", (dialog, which) -> {

                })
                .create();
    }

    private void sendResult(String tag,ArticleModel articleModel){
        if (getTargetFragment()==null){
            return;
        }
        Intent intent=new Intent();
        intent.setAction(tag);
        intent.putExtra(ARTICLE_MODEL_DATA,articleModel);
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK,intent);
    }


}
