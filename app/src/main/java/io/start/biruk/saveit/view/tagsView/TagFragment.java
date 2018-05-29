package io.start.biruk.saveit.view.tagsView;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.presenter.TagPresenter;
import io.start.biruk.saveit.view.widget.fastscroller.FastScroller;

/**
 * A simple {@link Fragment} subclass.
 */
public class TagFragment extends Fragment implements TagView {

    @Inject TagPresenter tagPresenter;
    @Inject Picasso picasso;

    @Bind(R.id.tag_recycler_view) RecyclerView tagRecyclerView;
    @Bind(R.id.tag_fast_scroller) FastScroller tagFastScroller;
    @Bind(R.id.empty_tag_description) TextView emptyTagTextView;
    @Bind(R.id.empty_tag_image) ImageView emptyImageView;

    public TagFragment() {
        // Required empty public constructor
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
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        tagPresenter.loadTags();
    }

    @Override
    public void displayTags(List<String> tags) {
        tagRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        tagRecyclerView.setAdapter(tagAdapter);
    }

    @Override
    public void displayEmptyTagView() {

    }
}
