package io.start.biruk.saveit.view.baseArticleView;

import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/13/2018.
 */
public interface BaseArticleView {

    void displayArticle(List<ArticleModel> articleModels);

    void displayEmptyArticlesView(String message);

    void launchArticleView(ArticleModel articleModel);

    void articleFavortieToggler(ArticleModel articleModel);

    void launchTagEditorView(ArticleModel articleModel);

    void launchArticleOptionsView(ArticleModel articleModel);

    void displayUpdateView(String msg);
}
