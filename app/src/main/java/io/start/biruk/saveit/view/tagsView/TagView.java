package io.start.biruk.saveit.view.tagsView;

import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/10/2018.
 */
public interface TagView {
    void showTagsView(String tagTitle, List<ArticleModel> articleModels);

    void emptyTagsView();

    void onTagSelected(String tagTitle, List<ArticleModel> articleModels);

    void onTagOptionSelected(String tagTitle, List<ArticleModel> articleModels);
}
