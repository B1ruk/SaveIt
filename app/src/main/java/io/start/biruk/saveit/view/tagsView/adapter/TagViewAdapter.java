package io.start.biruk.saveit.view.tagsView.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.TagSelectEvent;
import io.start.biruk.saveit.model.data.TagData;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.util.CoverArtUtil;
import io.start.biruk.saveit.view.tagsView.TagView;

/**
 * Created by biruk on 5/29/2018.
 */
public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.TagViewHolder> {

    @Inject Picasso picasso;

    private List<TagData> tags;
    private TagView.TagListener tagListener;

    public TagViewAdapter(TagView.TagListener tagListener) {
        this.tagListener = tagListener;
        this.tags = new ArrayList<>();

        App.getAppComponent().inject(this);
    }

    public void addTags(List<TagData> tags) {
        this.tags = tags;
        notifyDataSetChanged();
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_tag_view, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        TagData tagData = tags.get(position);

        List<String> coverArtPath = CoverArtUtil.getCoverArtPath(tagData.getArticleModels());
        if (!coverArtPath.isEmpty()) {
            picasso.load(new File(coverArtPath.get(0) + File.separator + "sc.png"))
                    .fit()
                    .into(holder.tagArticleCoverArt);
        }

        String tag = tagData.getTag();
        int size = tagData.getArticleModels().size();

        String sizeTxt = size > 1 ? size + " articles " : size + " article";

        holder.tagTitleView.setText(tag);
        holder.tagArticleCountView.setText(sizeTxt);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.tag_content_cover) ImageView tagArticleCoverArt;
        @Bind(R.id.tag_title) TextView tagTitleView;
        @Bind(R.id.tag_article_count) TextView tagArticleCountView;

        public TagViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new TagSelectEvent(tags.get(getAdapterPosition()).getTag()));
        }
    }
}
