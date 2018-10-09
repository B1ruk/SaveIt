package io.start.biruk.saveit.di.component;

import javax.inject.Singleton;

import dagger.Component;
import io.start.biruk.saveit.di.module.MainModule;
import io.start.biruk.saveit.presenter.ArticlePresenter;
import io.start.biruk.saveit.presenter.TagPresenter;
import io.start.biruk.saveit.service.ArticleFetcherService;
import io.start.biruk.saveit.view.MainActivity;
import io.start.biruk.saveit.view.articleView.articleOptions.AddTagDialog;
import io.start.biruk.saveit.view.baseArticleView.ArticleFragment;
import io.start.biruk.saveit.view.dialog.StatusDialog;
import io.start.biruk.saveit.view.searchView.SearchActivity;
import io.start.biruk.saveit.view.tagsView.SelectedTagActivity;
import io.start.biruk.saveit.view.tagsView.TagFragment;
import io.start.biruk.saveit.view.tagsView.adapter.TagViewAdapter;

/**
 * Created by biruk on 5/26/2018.
 */
@Component(modules = {MainModule.class})
@Singleton
public interface MainComponent {

    void inject(ArticlePresenter articlePresenter);

    void inject(TagPresenter tagPresenter);

    void inject(ArticleFetcherService articleFetcherService);

    void inject(SelectedTagActivity selectedTagActivity);

    void inject(TagFragment tagFragment);

    void inject(ArticleFragment articleFragment);

    void inject(SearchActivity searchActivity);

    void inject(MainActivity mainActivity);

    void inject(AddTagDialog addTagDialog);

    void inject(TagViewAdapter tagViewAdapter);

    void inject(StatusDialog statusDialog);
}
