package io.start.biruk.saveit.view.articleView.articleAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.util.CoverArtUtil;
import io.start.biruk.saveit.util.FontUtil;
import io.start.biruk.saveit.util.TagStringUtil;
import io.start.biruk.saveit.view.listener.ArticleClickListener;
import io.start.biruk.saveit.view.widget.CircleTransform;

/**
 * Created by biruk on 5/15/2018.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<ArticleModel> articleModels;
    private Context context;
    private ArticleClickListener articleClickListener;


    public ArticleAdapter(Context context, ArticleClickListener articleClickListener) {
        this.context = context;
        this.articleClickListener = articleClickListener;
        this.articleModels = new ArrayList<>();
    }

    public void setArticleData(List<ArticleModel> articleModel){
        this.articleModels =articleModel;
        notifyDataSetChanged();
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_article_view, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        ArticleModel articleModel = this.articleModels.get(position);

        String imgPath=articleModel.getPath()+File.separator+"sc.png";

        holder.articleTitleView.setText(articleModel.getTitle());
        holder.articleSavedDateView.setText(articleModel.getSavedDate());
        holder.tagContentView.setText(TagStringUtil.getFormatedTag(articleModel.getTags()));

        if (articleModel.isFavorite()){
            holder.articleFavoriteView.setAlpha(1.0f);
            holder.articleFavoriteView.setColorFilter(context.getResources().getColor(R.color.colorFav));
        }else {
            holder.articleFavoriteView.setColorFilter(context.getResources().getColor(R.color.noFavoriteIcon));
            holder.articleFavoriteView.setAlpha(0.4f);
        }

        if (!CoverArtUtil.coverArtExists(articleModel.getPath())){
            holder.newArticleIndicator.setVisibility(View.VISIBLE);
        }else {
            holder.newArticleIndicator.setVisibility(View.GONE);
        }

        holder.articleFavoriteView.setOnClickListener(v -> articleClickListener.onArticleFavoriteToggleSelected(articleModel));

        Picasso.with(context)
                .load(new File(imgPath))
                .transform(new CircleTransform())
                .placeholder(R.drawable.default_bg)
                .resize(80,80)
                .into(holder.articleImg);
    }

    @Override
    public int getItemCount() {
        return articleModels.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @Bind(R.id.article_title) TextView articleTitleView;
        @Bind(R.id.article_text_date) TextView articleSavedDateView;
        @Bind(R.id.article_favorite_toggle) ImageView articleFavoriteView;
        @Bind(R.id.tag_content) TextView tagContentView;
        @Bind(R.id.article_img) ImageView articleImg;
        @Bind(R.id.new_article_indicator) View newArticleIndicator;


        public ArticleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            FontUtil.setFont(context,articleTitleView,"Bazar.ttf");
        }


        @Override
        public void onClick(View v) {
            ArticleModel articleModel = ArticleAdapter.this.articleModels.get(getAdapterPosition());
            articleClickListener.onArticleSelected(articleModel);
        }

        @Override
        public boolean onLongClick(View v) {
            ArticleModel articleModel = ArticleAdapter.this.articleModels.get(getAdapterPosition());
            articleClickListener.onArticleOptionsSelected(articleModel);
            return false;
        }
    }
}
