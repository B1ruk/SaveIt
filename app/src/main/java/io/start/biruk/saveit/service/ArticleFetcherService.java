package io.start.biruk.saveit.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Date;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.events.ArticleFetchCompletedEvent;
import io.start.biruk.saveit.events.FetchArticleEvent;
import io.start.biruk.saveit.events.SaveArticleEvent;
import io.start.biruk.saveit.model.articleFetcher.ArticleFetcher;
import io.start.biruk.saveit.model.articleFetcher.ArticleSaver;
import io.start.biruk.saveit.model.articleFetcher.responseFetcher.ResourceFetcher;
import io.start.biruk.saveit.model.articleFetcher.responseFetcher.ResourcesLinkFetcher;
import io.start.biruk.saveit.model.dao.ArticleDbRepository;
import io.start.biruk.saveit.model.dao.ArticleRepository;
import io.start.biruk.saveit.model.data.ResourceLink;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.util.DateUtil;

/**
 * Created by biruk on 5/12/2018.
 */
public class ArticleFetcherService extends Service {


    private static final String TAG = "ArticleFetcherService";

    private ArticleFetcher articleFetcher;
    private ArticleSaver articleSaver;
    private ResourceFetcher resourceFetcher;
    private ArticleRepository articleRepository;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fetchArticleEvent(FetchArticleEvent fetchArticleEvent) {
        String url = fetchArticleEvent.getUrl();
        fetchArticle(url);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void articleFetchCompleted(ArticleFetchCompletedEvent articleFetchCompletedEvent){
        String url = articleFetchCompletedEvent.getUrl();
        buildNotification(url);
    }

    private void buildNotification(String url) {

    }

    @Override
    public void onCreate() {
        Log.d(TAG, "service started");
        super.onCreate();

        articleFetcher = new ArticleFetcher();
        articleSaver = new ArticleSaver();
        articleRepository = new ArticleDbRepository(this);
        resourceFetcher = new ResourceFetcher();

        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void fetchArticle(String url) {
        articleFetcher.fetchIndexPage(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(@NonNull String response) {
                        saveArticleToStorage(url, response);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error parsing content!!!\n", e);
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
                        Log.d(TAG, String.format("saved the article %s @ %s", url, path));
                        saveArticle(url, response, path);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "error occurred while saving article to storage \n", e);
                    }
                });
    }

    public void saveArticle(String url, String response, String path) {

        Document responseDoc = Jsoup.parse(response);

        ArticleModel articleModel = new ArticleModel.ArticleBuilder()
                .url(url)
                .path(path)
                .title(responseDoc.title())
                .savedDate(DateUtil.getFormattedDate(new Date()))
                .isFavorite(false)
                .build();

        articleRepository.addArticle(articleModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(@NonNull Boolean status) {
                        Log.d(TAG, String.format("is article %s saved to storage %s", articleModel.toString(), status));
                        saveResourcesToStorage(url, responseDoc, path);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "something went wrong in saving article ", e);
                    }
                });
    }

    public void saveResourcesToStorage(String url, Document responseDoc, String path) {
        resourceFetcher.saveResponsesToStorage(url, responseDoc, path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Boolean>() {
                    @Override
                    public void onSuccess(@NonNull Boolean status) {
                        Log.d(TAG, String.format("%s is saved -> %s ", responseDoc.title(), status));
                        EventBus.getDefault().post(new ArticleFetchCompletedEvent(url));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "something went wrong in saving the resources", e);
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
