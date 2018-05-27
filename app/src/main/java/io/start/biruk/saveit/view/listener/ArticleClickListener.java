package io.start.biruk.saveit.view.listener;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/17/2018.
 */
public interface ArticleClickListener {
    void onArticleSelected(ArticleModel articleModel);

    void onArticleOptionsSelected(ArticleModel articleModel);

    void onArticleFavoriteToggleSelected(ArticleModel articleModel);

    void onArticleTagSelected(ArticleModel articleModel);
}
