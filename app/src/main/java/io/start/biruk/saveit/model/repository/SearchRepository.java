package io.start.biruk.saveit.model.repository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 6/3/2018.
 */
public interface SearchRepository {
    Single<List<ArticleModel>> searchArticle(String query);

    Observable<ArticleModel> searchByTag(String tag, List<ArticleModel> articleModels);

    Observable<ArticleModel> searchByTitle(String title,List<ArticleModel> articleModels);

    Observable<ArticleModel> searchByContent(String query,List<ArticleModel> articleModels);

}
