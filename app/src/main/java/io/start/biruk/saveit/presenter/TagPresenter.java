package io.start.biruk.saveit.presenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.model.dao.ArticleRepository;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.view.tagsView.TagView;

/**
 * Created by biruk on 5/10/2018.
 */
public class TagPresenter {
    private ArticleRepository articleRepository;
    private Scheduler uiThread;


    private TagView tagView;

    @Inject
    public TagPresenter(ArticleRepository articleRepository, Scheduler uiThread) {
        this.articleRepository = articleRepository;
        this.uiThread = uiThread;
    }

    public void attachView(TagView tagView){
        this.tagView=tagView;
    }


    public void loadTags(){
        articleRepository.getAllArticles()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(ArticleModel::getTags)
                .filter(tags->!tags.isEmpty())
                .distinct()
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<List<String>>() {
                    @Override
                    public void onSuccess(@NonNull List<String> tags) {
                        if (!tags.isEmpty()){
                            tagView.displayTags(tags);
                        }else {
                            tagView.displayEmptyTagView();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }

    public void dettachView(TagView tagView){
        this.tagView=tagView;
    }


}
