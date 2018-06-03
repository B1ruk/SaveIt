package io.start.biruk.saveit.di.module;

import android.content.Context;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.start.biruk.saveit.model.articleFetcher.ArticleFetcher;
import io.start.biruk.saveit.model.articleFetcher.ArticleSaver;
import io.start.biruk.saveit.model.articleFetcher.responseFetcher.ResourceFetcher;
import io.start.biruk.saveit.model.repository.SearchRepository;
import io.start.biruk.saveit.model.repository.SearchRepositoryImpl;
import io.start.biruk.saveit.model.repository.TagRepository;
import io.start.biruk.saveit.model.repository.dao.ArticleDbRepository;
import io.start.biruk.saveit.model.repository.ArticleRepository;
import io.start.biruk.saveit.model.repository.dao.TagRepositoryImpl;

/**
 * Created by biruk on 5/26/2018.
 */
@Module
public class MainModule {
    private Context appContext;

    public MainModule(Context appContext) {
        this.appContext = appContext;
    }

    @Provides
    @Singleton
    public Context providesAppContext() {
        return appContext;
    }

    @Provides
    @Singleton
    public Picasso providesContext(Context context){
        return Picasso.with(context);
    }

    @Provides
    @Singleton
    public ArticleRepository providesArticleRepository(Context context) {
        return new ArticleDbRepository(context);
    }

    @Provides
    @Singleton
    public TagRepository providesTagRepository(ArticleRepository articleRepository){
        return new TagRepositoryImpl(articleRepository);
    }

    @Provides
    @Singleton
    public SearchRepository providesSearchRepository(ArticleRepository articleRepository){
        return new SearchRepositoryImpl(articleRepository);
    }

    @Provides
    @Singleton
    public Scheduler providesUiThread() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    public ResourceFetcher providesResourceFetcher() {
        return new ResourceFetcher();
    }

    @Provides
    @Singleton
    public ArticleSaver providesArticleSaver() {
        return new ArticleSaver();
    }

    @Provides
    @Singleton
    public ArticleFetcher providesArticleFetcher() {
        return new ArticleFetcher();
    }

}
