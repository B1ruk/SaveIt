package io.start.biruk.saveit.model.repository;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 6/3/2018.
 */
public class SearchRepositoryImpl implements SearchRepository {

    private ArticleRepository articleRepository;

    public SearchRepositoryImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Single<List<ArticleModel>> searchArticle(String query) {

        return articleRepository.getAllArticles()
                .flatMap(articleModels -> Observable.merge(
                        searchByTitle(query, articleModels),
                        searchByTag(query, articleModels))
                        .sorted(this::sort)
                        .toList());
    }

    @Override
    public Observable<ArticleModel> searchByTag(String tag, List<ArticleModel> articleModels) {
        return Observable.fromIterable(articleModels)
                .filter(articleModel -> articleModel.getTags().contains(tag));
    }

    @Override
    public Observable<ArticleModel> searchByTitle(String query, List<ArticleModel> articleModels) {
        return Observable.fromIterable(articleModels)
                .filter(articleModel -> articleModel.getTitle().contains(query));
    }

    @Override
    public Observable<ArticleModel> searchByContent(String tag, List<ArticleModel> articleModels) {
        return null;
    }


    private int sort(ArticleModel articleModel, ArticleModel articleModel1) {
        return articleModel.getTitle().compareTo(articleModel1.getTitle());
    }

}
