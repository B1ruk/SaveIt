package io.start.biruk.saveit.model.articleFetcher;


import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Date;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.events.ArticleFetchCompletedEvent;
import io.start.biruk.saveit.indicator.NotificationIndicator;
import io.start.biruk.saveit.model.articleFetcher.responseFetcher.ResourceFetcher;
import io.start.biruk.saveit.model.repository.ArticleRepository;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.util.DateUtil;

/**
 * Created by biruk on 5/26/2018.
 * entry point for saving an article
 */
public class ArticleMainSaver {
    private ArticleFetcher articleFetcher;
    private ArticleSaver articleSaver;
    private ResourceFetcher resourceFetcher;
    private ArticleRepository articleRepository;

    private ArticleMainSaver.CallBack mainCallback;

    @Inject
    public ArticleMainSaver(ArticleFetcher articleFetcher, ArticleSaver articleSaver, ResourceFetcher resourceFetcher, ArticleRepository articleRepository) {
        this.articleFetcher = articleFetcher;
        this.articleSaver = articleSaver;
        this.resourceFetcher = resourceFetcher;
        this.articleRepository = articleRepository;
    }

    public void addCallBack(ArticleMainSaver.CallBack callBack) {
        this.mainCallback = callBack;
        this.resourceFetcher.setCallBack(callBack);
    }

    public void fetchArticle(String url) {
        mainCallback.init();


        articleFetcher.fetchIndexPage(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(@NonNull String response) {
                        mainCallback.update(NotificationIndicator.ON_GOING, String.format("%s is cached", url));
                        saveArticleToStorage(url, response);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mainCallback.update(NotificationIndicator.ERROR, e.getMessage());
                    }
                });
    }

    public void saveArticleToStorage(String url, String response) {
        articleSaver.createArticleSingle(url, response)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(@NonNull String path) {

                        saveArticle(url, response, path);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void saveArticle(String url, String response, String path) {

        Document responseDoc = Jsoup.parse(response);

        ArticleModel articleModel = new ArticleModel.Builder()
                .url(url)
                .path(path)
                .title(responseDoc.title())
                .savedDate(DateUtil.getFormattedDate(new Date()))
                .isFavorite(false)
                .tags("")
                .build();

        articleRepository.addArticle(articleModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(@NonNull Boolean status) {
                        mainCallback.update(NotificationIndicator.ON_GOING, String.format("Saving %s to disk", url));
                        saveResourcesToStorage(url, responseDoc, path);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    public void saveResourcesToStorage(String url, Document responseDoc, String path) {
        resourceFetcher.saveResourcesToStorage(url, responseDoc, path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(@NonNull Boolean status) {
                        mainCallback.update(NotificationIndicator.COMPLETE, String.format("%s is saved to storage", url));
                        EventBus.getDefault().post(new ArticleFetchCompletedEvent(url));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mainCallback.update(NotificationIndicator.ERROR, e.getMessage());
                    }
                });
    }

    public interface CallBack {
        void init();

        void update(NotificationIndicator indicator, String msg);
    }

}
