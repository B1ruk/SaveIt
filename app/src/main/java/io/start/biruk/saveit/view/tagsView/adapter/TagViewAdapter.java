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
import io.start.biruk.saveit.view.tagsView.TagView;
import io.start.biruk.saveit.view.widget.fastscroller.BubbleTextGetter;

/**
 * Created by biruk on 5/29/2018.
 */
public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.TagViewHolder> implements BubbleTextGetter{

    @Inject Picasso picasso;

    private List<TagData> tags;
    private TagView.TagListener tagListener;

    public TagViewAdapter(TagView.TagListener tagListener){
        this.tagListener = tagListener;
        this.tags=new ArrayList<>();

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
        String tag = tags.get(position).getTag();
        int size = tags.get(position).getArticleModels().size();

        String sizeTxt="";
        if (size==1){
            sizeTxt=size+ " article";
        }else {
            sizeTxt=size+ "articles";
        }

        holder.tagTitleView.setText(tag);
        holder.tagArticleCountView.setText(sizeTxt);

        List<String> coverArts = Stream.of(tags.get(position).getArticleModels())
                .map(ArticleModel::getPath)
                .filter(path -> new File(path + File.separator + "sc.png").exists())
                .toList();

        picasso.load(new File(coverArts.get(0)+File.separator + "sc.png"))
                .placeholder(R.drawable.default_bg)
                .resize(140,140)
                .into(holder.tagArticleCoverArt);
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return String.valueOf(tags.get(pos).getTag().toUpperCase().charAt(0));
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
            EventBus.getDefault().post(new TagSelectEvent(tags.get(getAdapterPosition()).getTag()));
        }
    }
}
