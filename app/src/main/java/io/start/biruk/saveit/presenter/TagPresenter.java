package io.start.biruk.saveit.presenter;

import com.annimon.stream.Stream;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.model.data.TagData;
import io.start.biruk.saveit.model.repository.TagRepository;
import io.start.biruk.saveit.view.tagsView.TagView;

/**
 * Created by biruk on 5/10/2018.
 */
public class TagPresenter {

    private TagRepository tagRepository;
    private Scheduler uiThread;

    private TagView tagView;

    @Inject
    public TagPresenter(TagRepository tagRepository, Scheduler uiThread) {
        this.tagRepository = tagRepository;
        this.uiThread = uiThread;
    }

    public void attachView(TagView tagView) {
        this.tagView = tagView;
    }

    public void loadTags() {
        tagRepository.getAllTagsSingle()
                .subscribeOn(Schedulers.io())
                .observeOn(uiThread)
                .subscribeWith(new DisposableSingleObserver<List<TagData>>() {
                    @Override
                    public void onSuccess(@NonNull List<TagData> tagDatas) {
                        if (!tagDatas.isEmpty()){
                            tagView.displayTags(sortList(tagDatas));
                        } else {
                            tagView.displayEmptyTagView();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }

    private List<TagData> sortList(List<TagData> tagDatas) {
        return Stream.of(tagDatas)
                .sortBy(TagData::getTag)
                .toList();

    }

    public void dettachView(TagView tagView) {
        this.tagView = tagView;
    }


}
