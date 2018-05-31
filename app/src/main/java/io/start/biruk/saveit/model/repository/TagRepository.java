package io.start.biruk.saveit.model.repository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.start.biruk.saveit.model.data.TagData;
import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/31/2018.
 */
public interface TagRepository  {
    Single<List<TagData>> getAllTagsSingle();
    Observable<List<TagData>> getAllTagsObservable();
}
