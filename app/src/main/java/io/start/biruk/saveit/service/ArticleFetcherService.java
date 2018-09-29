package io.start.biruk.saveit.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.FetchArticleEvent;
import io.start.biruk.saveit.events.UrlFromClipboardEvent;
import io.start.biruk.saveit.indicator.NotificationIndicator;
import io.start.biruk.saveit.model.articleFetcher.ArticleMainSaver;
import io.start.biruk.saveit.util.HttpUtil;
import io.start.biruk.saveit.view.displayArticleView.DisplayArticleActivity;

/**
 * Created by biruk on 5/12/2018.
 */
public class ArticleFetcherService extends Service implements ArticleMainSaver.CallBack {


    private static final String TAG = "ArticleFetcherService";
    private static final int NOTIFICATION_ID = 3870;

    private NotificationManager notificationManager;
    private RemoteViews largeRemoteViews = null;

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
    }

    @Override
    public void update(NotificationIndicator indicator, String msg) {
        updateNotification(indicator, msg);
    }

    public void updateNotification(NotificationIndicator indicator, String msg) {
        notificationManager.notify(NOTIFICATION_ID, buildNotification(indicator, msg).build());
    }

    public NotificationCompat.Builder buildNotification(NotificationIndicator indicator, String msg) {
        Intent dispArt = new Intent(this, DisplayArticleActivity.class);

        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.book_outline)
                .setCustomBigContentView(setUpBigContentView(indicator, msg))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setOngoing(true);
    }

    private RemoteViews setUpBigContentView(NotificationIndicator indicator, String msg) {
        if (this.largeRemoteViews == null) {
            initRemoteViews();
        }
        updateRemoteViews(indicator, msg);
        return largeRemoteViews;
    }


    public void initRemoteViews() {
        this.largeRemoteViews = new RemoteViews(getPackageName(), R.layout.remoteview_layout_big);
        this.largeRemoteViews.setViewVisibility(R.id.remote_progress,View.VISIBLE);
        updateRemoteViews(NotificationIndicator.ON_GOING,"Parsing page ...");
    }

    public void updateRemoteViews(NotificationIndicator indicator, String msg) {
        switch (indicator) {
            case ON_GOING:
                largeRemoteViews.setTextViewText(R.id.remote_title, "Saving page resource ...");
                largeRemoteViews.setProgressBar(R.id.remote_progress, 1, 1, true);
                largeRemoteViews.setTextViewText(R.id.remote_content, msg);
                break;
            case ERROR:
                largeRemoteViews.setTextViewText(R.id.remote_title, "Something went wrong ...");
                largeRemoteViews.setProgressBar(R.id.remote_progress, 1, 1, true);
                largeRemoteViews.setTextViewText(R.id.remote_content, msg);
                break;
            case COMPLETE:
                largeRemoteViews.setViewVisibility(R.id.remote_progress, View.GONE);
                largeRemoteViews.setTextViewText(R.id.remote_content, msg);
                largeRemoteViews.setTextViewText(R.id.remote_title, "Page is saved");
                break;
        }
    }

}
