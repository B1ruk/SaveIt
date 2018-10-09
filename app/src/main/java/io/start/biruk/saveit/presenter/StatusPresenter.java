package io.start.biruk.saveit.presenter;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.model.repository.ArticleRepository;
import io.start.biruk.saveit.util.FileSizeUtil;
import io.start.biruk.saveit.view.statusView.StatusView;

/**
 * Created by biruk on 02/10/18.
 */

public class StatusPresenter {

    private ArticleRepository articleRepository;
    private Scheduler uiThread;

    private StatusView statusView;

    private Long totalFolderSize;

    @Inject
    public StatusPresenter(ArticleRepository articleRepository, Scheduler uiThread) {
        this.articleRepository = articleRepository;
        this.uiThread = uiThread;
    }

    public void attachView(StatusView statusView) {
        this.statusView = statusView;

        this.totalFolderSize = 0L;
    }


    public void loadArticlesCount() {
        articleRepository.getAllArticlesObser()
                .count()
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(@NonNull Long count) {
                        String msg = "";
                        if (count > 1) {
                            msg = String.format("%s Articles", count);
                        } else if (count == 1) {
                            msg = String.format("%s Article", count);
                        }
                        statusView.displayArticleCount(msg);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        statusView.displayError("unable to get Article count", e);
                    }
                });
    }

    public void loadArticlesTotalSize() {
        articleRepository.getAllArticlesObser()
                .map(ArticleModel::getPath)
                .flatMap(path -> FileSizeUtil.computeFolderSizeSingle(path).toObservable())
                .reduce((size1, size2) -> size1 + size2)
                .toSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Long>() {
                    @Override
                    public void onSuccess(@NonNull Long size) {
                        statusView.displayTotalSize(FileSizeUtil.sizeFormater(size));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        statusView.displayError("unable to get error msg", e);
                    }
                });

    }

    public void detachView() {
        this.statusView = null;
    }
}
