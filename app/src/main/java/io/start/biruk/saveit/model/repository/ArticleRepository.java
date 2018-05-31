package io.start.biruk.saveit.model.repository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/10/2018.
 */
public interface ArticleRepository {
    Single<Boolean> addArticle(ArticleModel articleModel);

    Single<List<ArticleModel>> getAllArticles();

    Observable<ArticleModel> getAllArticlesObser();

    boolean removeArticle(ArticleModel articleModel);

    Single<Integer> updateArticle(ArticleModel articleModel);

    Single<Integer> deleteArticle(ArticleModel articleModel);
}
