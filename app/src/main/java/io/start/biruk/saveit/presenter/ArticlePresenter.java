package io.start.biruk.saveit.presenter;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.model.repository.ArticleRepository;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.util.DateUtil;
import io.start.biruk.saveit.util.FileUtil;
import io.start.biruk.saveit.util.TagStringUtil;
import io.start.biruk.saveit.view.baseArticleView.ArticleView;

/**
 * Created by biruk on 5/17/2018.
 */
public class ArticlePresenter {

    private ArticleRepository articleRepository;
    private Scheduler uiThread;

    private ArticleView articleView;

    @Inject
    public ArticlePresenter(ArticleRepository articleRepository, Scheduler uiThread) {
        this.articleRepository = articleRepository;
        this.uiThread = uiThread;
    }

    public void attachView(ArticleView view) {
        this.articleView = view;
    }

    public void onArticleSelected(ArticleModel articleModel) {
        articleView.launchArticleView(articleModel);
    }

    public void onArticleOptionSelected(ArticleModel articleModel) {
        articleView.launchArticleOptionsView(articleModel);
    }


    public void onFavoriteToggleSelected(ArticleModel articleModel) {
        ArticleModel modifiedArticleModel = new ArticleModel.Builder()
                .url(articleModel.getUrl())
                .title(articleModel.getTitle())
                .path(articleModel.getPath())
                .tags(articleModel.getTags())
                .savedDate(articleModel.getSavedDate())
                .isFavorite(!articleModel.isFavorite())
                .build();

        articleRepository.updateArticle(modifiedArticleModel)
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@NonNull Integer updateStatus) {
                        articleView.updateView();
                        if (modifiedArticleModel.isFavorite()) {
                            articleView.displayUpdateView("Article added to favorite.");
                        } else {
                            articleView.displayUpdateView("Article removed from favorite.");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    public void deleteArticle(ArticleModel articleModel) {

        FileUtil.deletePath(new File(articleModel.getPath()));

        articleRepository.deleteArticle(articleModel)
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        articleView.displayUpdateView("Article Deleted");
                        articleView.updateView();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    public void updateArticle(ArticleModel articleModel, String tag) {


        ArticleModel updatedArticle = new ArticleModel.Builder()
                .title(articleModel.getTitle())
                .url(articleModel.getUrl())
                .path(articleModel.getPath())
                .isFavorite(articleModel.isFavorite())
                .savedDate(articleModel.getSavedDate())
                .tags(TagStringUtil.appendTag(articleModel.getTags(), tag))
                .build();

        this.updateArticle(updatedArticle);
    }

    public void updateArticle(ArticleModel articleModel) {
        articleRepository.updateArticle(articleModel)
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<Integer>() {
                    @Override
                    public void onSuccess(@NonNull Integer integer) {
                        articleView.displayUpdateView("Article updated");
                        articleView.updateView();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void loadArticles(List<ArticleModel> articleModels) {
        if (!articleModels.isEmpty()) {
            articleView.displayArticle(articleModels);
        } else {
            articleView.displayEmptyArticlesView("empty articles");
        }
    }

    public void loadAllArticles() {
        articleRepository.getAllArticles()
                .map(this::sortArticles)
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<List<ArticleModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ArticleModel> articleModels) {
                        if (!articleModels.isEmpty()) {
                            articleView.displayArticle(articleModels);
                        } else {
                            articleView.displayEmptyArticlesView("empty articles");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    public void loadFavoriteArticles() {
        articleRepository.getAllArticlesObser()
                .filter(ArticleModel::isFavorite)
                .toList()
                .map(this::sortArticles)
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<List<ArticleModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ArticleModel> articleModels) {
                        if (!articleModels.isEmpty()) {
                            articleView.displayArticle(articleModels);
                        } else {
                            articleView.displayEmptyArticlesView("empty articles view");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }


    public List<ArticleModel> sortArticles(List<ArticleModel> articles) {

        List<ArticleModel> articleModels = Stream.of(articles)
                .sortBy(articleModel -> DateUtil.parseToDate(articleModel.getSavedDate()))
                .toList();

        Collections.reverse(articleModels);     //descending order

        return articleModels;
    }


    public void loadArticlesByQuery(String msg) {

    }

    public void loadArticlesByTag(String tag) {

    }

    public void detachView() {
        this.articleView = null;
    }


}
