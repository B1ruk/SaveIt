package io.start.biruk.saveit.events;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/13/2018.
 */
public class SaveArticleEvent {

    private ArticleModel articleModel;

    public SaveArticleEvent(ArticleModel articleModel) {
        this.articleModel = articleModel;
    }

    public ArticleModel getArticleModel() {
        return articleModel;
    }
}
