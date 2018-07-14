package io.start.biruk.saveit.view.tagsView;

import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 14/07/18.
 */

public interface SelectedTagView {
    void selectedTag(String tag);

    void displayArticles(List<ArticleModel> articleModels);
}
