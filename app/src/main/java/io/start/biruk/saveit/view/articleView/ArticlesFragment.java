package io.start.biruk.saveit.view.articleView;


import android.support.v4.app.Fragment;

import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.view.baseArticleView.BaseArticleFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticlesFragment extends BaseArticleFragment {


    public ArticlesFragment() {
        // Required empty public constructor
    }

    @Override
    public void displayArticle(List<ArticleModel> articleModels) {
        super.displayArticle(articleModels);
    }
}
