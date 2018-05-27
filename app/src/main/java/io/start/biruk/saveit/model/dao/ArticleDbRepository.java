package io.start.biruk.saveit.model.dao;

import android.content.Context;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.start.biruk.saveit.model.db.ArticleDbHelper;
import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/13/2018.
 */
public class ArticleDbRepository implements ArticleRepository {
    private ArticleDbHelper articleDbHelper;

    public ArticleDbRepository(Context context) {
        this.articleDbHelper = new ArticleDbHelper(context);
    }


    @Override
    public Single<Boolean> addArticle(ArticleModel articleModel) {
        return Single.fromCallable(() -> articleDbHelper.getArticleDao().createOrUpdate(articleModel))
                .map(status -> status.isCreated() || status.isUpdated());
    }

    public Single<List<ArticleModel>> getAllArticles() {
        return Observable.defer(() -> Observable.fromIterable(articleDbHelper.getArticleDao().queryForAll()))
                .filter(articleModel -> new File(articleModel.getPath()).exists())
                .toList();
    }


    @Override
    public boolean removeArticle(ArticleModel articleModel) {
        return false;
    }

    @Override
    public boolean updateArticle(ArticleModel articleModel) {
        return false;
    }

    @Override
    public boolean deleteArticle(ArticleModel articleModel) {
        return false;
    }
}
