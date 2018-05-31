package io.start.biruk.saveit.view.tagsView.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.model.data.TagData;

/**
 * Created by biruk on 5/29/2018.
 */
public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.TagViewHolder> {

    private List<TagData> tags;


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
        String tag = tags.get(position).getTag();
        int size = tags.get(position).getArticleModels().size();

        holder.tagTitleView.setText(tag);
        holder.tagArticleCountView.setText(Integer.toString(size));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.tag_content_cover) ImageView tagArticleCoverArt;
        @Bind(R.id.tag_title) TextView tagTitleView;
        @Bind(R.id.tag_article_count) TextView tagArticleCountView;

        public TagViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
