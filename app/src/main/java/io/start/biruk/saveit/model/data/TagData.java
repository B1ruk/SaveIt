package io.start.biruk.saveit.model.data;

import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/31/2018.
 */
public class TagData {
    private String tag;
    private List<ArticleModel> articleModels;

    public TagData(String tag, List<ArticleModel> articleModels) {
        this.tag = tag;
        this.articleModels = articleModels;
    }

    public String getTag() {
        return tag;
    }

    public List<ArticleModel> getArticleModels() {
        return articleModels;
    }
}
