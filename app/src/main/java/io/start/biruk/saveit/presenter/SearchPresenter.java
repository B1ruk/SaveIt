package io.start.biruk.saveit.presenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.model.repository.SearchRepository;
import io.start.biruk.saveit.view.searchView.SearchArticleView;

/**
 * Created by biruk on 6/3/2018.
 */
public class SearchPresenter {
    private SearchRepository searchRepository;
    private Scheduler uiThread;

    private SearchArticleView searchArticleView;

    @Inject
    public SearchPresenter(SearchRepository searchRepository, Scheduler uiThread) {
        this.searchRepository = searchRepository;
        this.uiThread = uiThread;
    }

    public void attachView(SearchArticleView searchArticleView){
        this.searchArticleView = searchArticleView;
    }

    public void loadArticleSearchResults(String query){
        searchRepository.searchArticle(query)
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<List<ArticleModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ArticleModel> articleModels) {
                        if (!articleModels.isEmpty()){
                            searchArticleView.showSearchResults(articleModels);
                        }
                        else if (articleModels.isEmpty()){
                            loadAdvacnedArticleSearchResults(query);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private void loadAdvacnedArticleSearchResults(String query) {
        searchRepository.searchByContent(query)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<List<ArticleModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ArticleModel> articleModels) {
                        searchArticleView.showSearchResults(articleModels);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }


    public void detachView(SearchArticleView searchArticleView){
        this.searchArticleView =null;
    }
}
