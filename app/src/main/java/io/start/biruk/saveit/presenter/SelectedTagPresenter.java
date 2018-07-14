package io.start.biruk.saveit.presenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.model.data.TagData;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.model.repository.ArticleRepository;
import io.start.biruk.saveit.model.repository.TagRepository;
import io.start.biruk.saveit.view.tagsView.SelectedTagView;

/**
 * Created by biruk on 14/07/18.
 */

public class SelectedTagPresenter {

    private TagRepository tagRepository;

    private Scheduler uiThread;

    private SelectedTagView selectedTagView;

    @Inject
    public SelectedTagPresenter(TagRepository tagRepository, Scheduler uiThread) {
        this.tagRepository = tagRepository;
        this.uiThread = uiThread;
    }

    public void attachView(SelectedTagView selectedTagView){
        this.selectedTagView=selectedTagView;
    }

    public void loadArticles(String tag){
        tagRepository.getAllTagsObservable()
                .flatMap(tagData -> Observable.fromIterable(tagData)
                        .filter(tagModel->tagModel.getTag().equals(tag))
                        .map(TagData::getArticleModels))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<ArticleModel>>() {
                    @Override
                    public void onNext(@NonNull List<ArticleModel> articleModels) {
                        selectedTagView.displayArticles(articleModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void detachView(){
        this.selectedTagView=null;
    }

}
