package io.start.biruk.saveit.view.tagsView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.R;

/**
 * Created by biruk on 5/28/2018.
 */
public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private TagView.TagListener tagListener;
    private List<String> tags;

    public TagAdapter(TagView.TagListener tagListener,List<String> tags) {
        this.tagListener = tagListener;
        this.tags = tags;
    }

    @Override
    public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_article_option_tag_list, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TagViewHolder holder, int position) {
        String tags = this.tags.get(position);
        holder.tagTitleView.setText(tags);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.article_option_tag_title) TextView tagTitleView;

        public TagViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View v) {
            tagListener.onTagSelected(tags.get(getAdapterPosition()));
        }
    }
}
