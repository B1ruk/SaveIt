package io.start.biruk.saveit.model.repository;

import io.reactivex.Observable;
import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 6/3/2018.
 */
public interface SearchRepository {
    Observable<ArticleModel> searchArticle(String query);

    Observable<ArticleModel> searchByTag(String tag);

    Observable<ArticleModel> searchByTitle(String tag);

    Observable<ArticleModel> searchByContent(String tag);

}
