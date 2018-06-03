package io.start.biruk.saveit.model.repository;

import io.reactivex.Observable;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.view.searchView.SearchView;

/**
 * Created by biruk on 6/3/2018.
 */
public class SearchRepositoryImpl implements SearchRepository{

    private ArticleRepository articleRepository;

    public SearchRepositoryImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Observable<ArticleModel> searchArticle(String query) {
        return null;
    }

    @Override
    public Observable<ArticleModel> searchByTag(String tag) {
        return null;
    }

    @Override
    public Observable<ArticleModel> searchByTitle(String tag) {
        return null;
    }

    @Override
    public Observable<ArticleModel> searchByContent(String tag) {
        return null;
    }
}
