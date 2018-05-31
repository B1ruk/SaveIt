package io.start.biruk.saveit.model.repository.dao;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.start.biruk.saveit.model.data.TagData;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.model.repository.ArticleRepository;
import io.start.biruk.saveit.model.repository.TagRepository;
import io.start.biruk.saveit.util.StringUtil;

/**
 * Created by biruk on 5/31/2018.
 */
public class TagRepositoryImpl implements TagRepository {
    private ArticleRepository articleRepository;

    public TagRepositoryImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Single<List<TagData>> getAllTagsSingle() {
        return articleRepository.getAllArticlesObser()
                .map(ArticleModel::getTags)
                .filter(tags -> !tags.isEmpty())
                .flatMap(tags -> Observable.fromIterable(StringUtil.getTagList(tags)))
                .distinct()
                .flatMap(this::toTagData)
                .toList();
    }

    private Observable<TagData> toTagData(final String tag) {
        return articleRepository.getAllArticlesObser()
                .filter(articleModel -> articleModel.getTags().contains(tag))
                .toList()
                .map(articleModels ->new TagData(tag,articleModels))
                .toObservable();
    }


    @Override
    public Observable<List<TagData>> getAllTagsObservable() {
        return getAllTagsSingle().toObservable();
    }
}
