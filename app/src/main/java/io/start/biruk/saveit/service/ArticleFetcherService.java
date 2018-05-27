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
import io.start.biruk.saveit.model.articleFetcher.ArticleMainSaver;
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
public class ArticleFetcherService extends Service implements ArticleMainSaver.CallBack{


    private static final String TAG = "ArticleFetcherService";



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fetchArticleEvent(FetchArticleEvent fetchArticleEvent) {
        String url = fetchArticleEvent.getUrl();
//        fetchArticle(url);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onArticleSaveError(String msg) {

    }

    @Override
    public void onLoadingResourcesStarted(String msg) {

    }

    @Override
    public void onArticleSaved(String msg) {

    }

    @Override
    public void onResourceFetchError(String msg) {

    }

    @Override
    public void onArticleFetchStarted(String msg) {

    }
}
