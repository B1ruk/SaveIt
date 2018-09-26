package io.start.biruk.saveit.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
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
    private static final int NOTIFICATION_ID = 3870;

    private NotificationManager notificationManager;
    private Notification.Builder notificationBuilder;

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
            if (HttpUtil.isValid(text.toString())) {
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
    public void init() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new Notification.Builder(getApplicationContext());

        notificationBuilder.setSmallIcon(R.drawable.book_outline)
                .setPriority(Notification.PRIORITY_MAX)
                .setTicker("Saving page...")
                .setContentTitle("Parsing Page ...")
                .setProgress(0, 0, true)
                .setOnlyAlertOnce(true);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public void onArticleSaveError(Throwable error) {
        notificationBuilder.setSubText(error.getMessage())
                .setProgress(0, 0, true);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public void onArticleSaveProgressUpdater(String msg) {
        notificationBuilder.setSubText(msg)
                .setProgress(0, 0, true);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @Override
    public void onArticleSaveCompletion(String msg) {
        notificationBuilder
                .setContentTitle("Page is saved to disk")
                .setContentText(msg)
                .setProgress(0, 0, false);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }
}
