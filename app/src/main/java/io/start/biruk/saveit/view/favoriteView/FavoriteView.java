package io.start.biruk.saveit.view.favoriteView;

import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/10/2018.
 */
public interface FavoriteView {

    void showFavoriteArticlesView(List<ArticleModel> articleModels);

    void onArticlesOptionSelected(ArticleModel articleModel);

    void onArticleSelected(ArticleModel articleModel);

}
