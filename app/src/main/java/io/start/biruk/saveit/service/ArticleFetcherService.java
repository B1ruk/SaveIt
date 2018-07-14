package io.start.biruk.saveit.service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.start.biruk.saveit.App;
import io.start.biruk.saveit.events.ArticleFetchCompletedEvent;
import io.start.biruk.saveit.events.FetchArticleEvent;
import io.start.biruk.saveit.events.UrlFromClipboardEvent;
import io.start.biruk.saveit.model.articleFetcher.ArticleMainSaver;
import io.start.biruk.saveit.util.HttpUtil;

/**
 * Created by biruk on 5/12/2018.
 */
public class ArticleFetcherService extends Service implements ArticleMainSaver.CallBack {


    private static final String TAG = "ArticleFetcherService";

    @Inject
    ArticleMainSaver articleMainSaver;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fetchArticleEvent(FetchArticleEvent fetchArticleEvent) {
        String url = fetchArticleEvent.getUrl();
        articleMainSaver.fetchArticle(url);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "service started");
        super.onCreate();

        App.getAppComponent().inject(this);
        EventBus.getDefault().register(this);
        articleMainSaver.addCallBack(this);

        ClipboardManager clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.addPrimaryClipChangedListener(() -> {
            CharSequence text = clipboardManager.getText();
            if (HttpUtil.isValid(text.toString())){
                EventBus.getDefault().postSticky(new UrlFromClipboardEvent(text.toString()));
            }

        });

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
        EventBus.getDefault().post(new ArticleFetchCompletedEvent(msg));
    }

    @Override
    public void onResourceFetchError(String msg) {

    }

    @Override
    public void onArticleFetchStarted(String msg) {

    }
}
