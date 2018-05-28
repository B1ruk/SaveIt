package io.start.biruk.saveit.view.favoriteView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import io.start.biruk.saveit.App;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.presenter.ArticlePresenter;
import io.start.biruk.saveit.view.baseArticleView.BaseArticleFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends BaseArticleFragment{

    @Inject ArticlePresenter articlePresenter;

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
}
