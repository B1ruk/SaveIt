package io.start.biruk.saveit.view.favoriteView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.presenter.ArticlePresenter;
import io.start.biruk.saveit.view.baseArticleView.BaseArticleFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends BaseArticleFragment{

    @Inject ArticlePresenter articlePresenter;
    @Inject Picasso picasso;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getAppComponent().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        articlePresenter.attachView(this);
    }

    @Override
    public void updateView() {
        articlePresenter.loadFavoriteArticles();
    }

    @Override
    public void displayEmptyArticlesView(String message) {
        updateUi(0);

        picasso.load(R.drawable.ic_favorite_black_24dp)
                .resize(100,100)
                .into(emptyArticleImageView);

        emptyArticleTextView.setText("no favorite articles found");
    }
}
