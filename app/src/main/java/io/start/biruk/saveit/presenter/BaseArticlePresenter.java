package io.start.biruk.saveit.presenter;

import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.view.baseArticleView.BaseArticleView;
import io.start.biruk.saveit.view.listener.ArticleClickListener;

/**
 * Created by biruk on 5/17/2018.
 */
public class BaseArticlePresenter implements ArticleClickListener {

    private final BaseArticleView baseArticleView;

    public BaseArticlePresenter(BaseArticleView baseArticleView) {

        this.baseArticleView = baseArticleView;
    }

    @Override
    public void onArticleSelected(ArticleModel articleModel) {
        launchArticleView(articleModel);
    }

    @Override
    public void onArticleOptionsSelected(ArticleModel articleModel) {
        baseArticleView.launchArticleOptionsView(articleModel);
    }

    @Override
    public void onArticleFavoriteToggleSelected(ArticleModel articleModel) {
        baseArticleView.articleFavortieToggler(articleModel);
    }

    @Override
    public void onArticleTagSelected(ArticleModel articleModel) {
        baseArticleView.launchTagEditorView(articleModel);
    }


    private void launchArticleView(ArticleModel articleModel) {
        baseArticleView.launchArticleView(articleModel);
    }
}
