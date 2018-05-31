package io.start.biruk.saveit.view.tagsView;

import java.util.List;

import io.start.biruk.saveit.model.data.TagData;
import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/10/2018.
 */
public interface TagView {
    void displayTags(List<TagData> tags);
    void displayEmptyTagView();
    void onTagLoadError(Throwable e);

     interface TagListener{
        void onTagSelected(String tag);
    }
}
