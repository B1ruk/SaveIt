package io.start.biruk.saveit.model.dao;

import java.util.List;

import io.reactivex.Single;
import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/10/2018.
 */
public interface ArticleRepository {
    Single<Boolean> addArticle(ArticleModel articleModel);

    Single<List<ArticleModel>> getAllArticles();

    boolean removeArticle(ArticleModel articleModel);

    boolean updateArticle(ArticleModel articleModel);

    boolean deleteArticle(ArticleModel articleModel);
}
