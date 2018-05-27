package io.start.biruk.saveit.presenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.model.dao.ArticleRepository;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.view.baseArticleView.BaseArticleView;

/**
 * Created by biruk on 5/17/2018.
 */
public class ArticlePresenter {

    private ArticleRepository articleRepository;
    private Scheduler uiThread;

    private BaseArticleView baseArticleView;

    @Inject
    public ArticlePresenter(ArticleRepository articleRepository, Scheduler uiThread) {
        this.articleRepository = articleRepository;
        this.uiThread = uiThread;
    }

    public void attachView(BaseArticleView view) {
        this.baseArticleView = view;
    }

    public void onArticleSelected(ArticleModel articleModel) {
        baseArticleView.launchArticleView(articleModel);
    }

    public void onArticleOptionSelected(ArticleModel articleModel) {
        baseArticleView.launchArticleOptionsView(articleModel);
    }


    public void onFavoriteToggleSelected(ArticleModel articleModel) {
        ArticleModel modifiedArticleModel = new ArticleModel.Builder()
                .url(articleModel.getUrl())
                .title(articleModel.getTitle())
                .path(articleModel.getPath())
                .savedDate(articleModel.getSavedDate())
                .isFavorite(!articleModel.isFavorite())
                .build();

        articleRepository.updateArticle(modifiedArticleModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@NonNull Integer updateStatus) {
                        loadAllArticles();
                        if (modifiedArticleModel.isFavorite()){
                            baseArticleView.displayUpdateView("Article added to favorite.");
                        }else {
                            baseArticleView.displayUpdateView("Article removed from favorite.");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void loadAllArticles() {
        articleRepository.getAllArticles()
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<List<ArticleModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ArticleModel> articleModels) {
                        if (!articleModels.isEmpty()) {
                            baseArticleView.displayArticle(articleModels);
                        } else {
                            baseArticleView.displayEmptyArticlesView("empty articles");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    public void loadFavoriteArticlesView(String query) {
        articleRepository.getAllArticles()
                .toObservable()
                .flatMap(articleModels -> Observable.fromIterable(articleModels))
                .filter(articleModel -> articleModel.isFavorite())
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<List<ArticleModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ArticleModel> articleModels) {
                        if (!articleModels.isEmpty()) {
                            baseArticleView.displayArticle(articleModels);
                        }
                        baseArticleView.displayEmptyArticlesView("empty articles view");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    public void loadArticlesByQuery(String msg) {

    }

    public void loadArticlesByTag(String tag) {

    }

    public void dettachView() {
        this.baseArticleView = null;
    }


}
