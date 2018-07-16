package io.start.biruk.saveit.view.tagsView;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.TagSelectEvent;
import io.start.biruk.saveit.model.data.TagData;
import io.start.biruk.saveit.presenter.TagPresenter;
import io.start.biruk.saveit.view.tagsView.adapter.TagViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagFragment extends Fragment implements TagView {
    private static final String TAG = "TagFragment";

    @Inject TagPresenter tagPresenter;
    @Inject Picasso picasso;

    @Bind(R.id.tag_recycler_view) RecyclerView tagRecyclerView;
    @Bind(R.id.empty_tag_description) TextView emptyTagTextView;
    @Bind(R.id.empty_tag_image) ImageView emptyImageView;

    public TagFragment() {
        // Required empty public constructor
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTagSelect(TagSelectEvent tagSelectEvent){
        launchSelectedTagView(tagSelectEvent.getTag());
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getAppComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tag, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        tagPresenter.attachView(this);
        tagPresenter.loadTags();
        EventBus.getDefault().register(this);
    }

    @Override
    public void displayTags(List<TagData> tags) {
        initRecyclerView(tags);

        tagRecyclerView.setVisibility(View.VISIBLE);

        emptyTagTextView.setVisibility(View.GONE);
        emptyImageView.setVisibility(View.GONE);

    }

    private void initRecyclerView(List<TagData> tags) {
        TagViewAdapter tagAdapter=new TagViewAdapter(this::sendResult);
        tagAdapter.addTags(tags);

        tagRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        tagRecyclerView.setAdapter(tagAdapter);
    }


    private void launchSelectedTagView(String tag) {
        Intent launchSelectedTag=new Intent(getActivity(),SelectedTagActivity.class);
        launchSelectedTag.setAction(tag);
        startActivity(launchSelectedTag);
    }

    private void sendResult(String tag) {

    }

    @Override
    public void displayEmptyTagView() {

        emptyTagTextView.setText("No tags");
        tagRecyclerView.setVisibility(View.GONE);

        emptyTagTextView.setVisibility(View.VISIBLE);
        emptyImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTagLoadError(Throwable e) {
        Log.e(TAG,"error on loading ",e);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
