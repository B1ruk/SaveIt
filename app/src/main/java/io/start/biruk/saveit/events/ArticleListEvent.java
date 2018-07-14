package io.start.biruk.saveit.events;

import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 *
 *
 * An event class that will be used
 * in results,display the article content of a specific tag
 */

public class ArticleListEvent {

    private List<ArticleModel> articleModels;

    public ArticleListEvent(List<ArticleModel> articleModels) {
        this.articleModels = articleModels;
    }

    public List<ArticleModel> getArticleModels() {
        return articleModels;
    }
}
