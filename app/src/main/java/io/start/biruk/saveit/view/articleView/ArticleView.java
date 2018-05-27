package io.start.biruk.saveit.view.articleView;

import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/10/2018.
 */
public interface ArticleView {
    void articlesListView(List<ArticleModel> articleModels);

    void emptyArticlesView();

    void onArticleSelected(ArticleModel articleModel);

    void onArticleOptionsSelected(ArticleModel articleModel);

}
