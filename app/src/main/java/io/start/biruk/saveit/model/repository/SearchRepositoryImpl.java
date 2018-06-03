package io.start.biruk.saveit.model.repository;

import android.util.Log;

import com.google.common.io.Files;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 6/3/2018.
 */
public class SearchRepositoryImpl implements SearchRepository {

    private ArticleRepository articleRepository;

    public SearchRepositoryImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Single<List<ArticleModel>> searchArticle(String query) {

        return articleRepository.getAllArticles()
                .flatMap(articleModels -> Observable.merge(
                        searchByTitle(query, articleModels),
                        searchByTag(query, articleModels))
                        .sorted(this::sort)
                        .toList());
    }

    @Override
    public Observable<ArticleModel> searchByTag(String tag, List<ArticleModel> articleModels) {
        return Observable.fromIterable(articleModels)
                .filter(articleModel -> contains(articleModel, tag));
    }

    @Override
    public Observable<ArticleModel> searchByTitle(String title, List<ArticleModel> articleModels) {
        return Observable.fromIterable(articleModels)
                .filter(articleModel -> contains(articleModel, title));
    }

    @Override
    public Observable<ArticleModel> searchByContent(String tag) {
        return articleRepository.getAllArticlesObser()
                .filter(articleModel -> fileContainsQuery(articleModel, tag));
    }

    private boolean fileContainsQuery(ArticleModel articleModel, String query) throws IOException {
        String indexPagePath = articleModel.getPath() + File.separator + "index.html";
        String articleContent = getPathContent(indexPagePath)
                .toLowerCase();

        return articleContent
                .toLowerCase()
                .contains(query.toLowerCase());
    }

    private String getPathContent(String path) throws IOException {
        return Files.asByteSource(new File(path))
                .asCharSource(Charset.defaultCharset())
                .read();
    }

    private boolean contains(ArticleModel articleModel, String query) {
        return articleModel.getTitle().toLowerCase().contains(query.toLowerCase());
    }


    private int sort(ArticleModel articleModel, ArticleModel articleModel1) {
        return articleModel.getTitle().compareTo(articleModel1.getTitle());
    }

}
