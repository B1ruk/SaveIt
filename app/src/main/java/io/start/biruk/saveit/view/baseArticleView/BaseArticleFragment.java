package io.start.biruk.saveit.view.baseArticleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.ArticleFetchCompletedEvent;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.presenter.ArticlePresenter;
import io.start.biruk.saveit.view.articleView.articleAdapter.ArticleAdapter;
import io.start.biruk.saveit.view.articleView.articleOptions.ArticleInfoDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.ArticleOptionDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.BaseArticleDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.DeleteArticleDialog;
import io.start.biruk.saveit.view.displayArticleView.DisplayArticleActivity;
import io.start.biruk.saveit.view.listener.ArticleClickListener;
import io.start.biruk.saveit.view.widget.fastscroller.FastScroller;

/**
 * Created by biruk on 5/13/2018.
 */
public class BaseArticleFragment extends Fragment implements BaseArticleView, ArticleClickListener {

    private static final String TAG = "BaseArticleFragment";

    private static final int REQUEST_ARTICLE_OPTION = 0;
    private static final int REQUEST_EDIT_TITLE_OPTION = 7;
    private static final int DELETE_ARTICLE_OPTION = 8;


    @Inject ArticlePresenter articlePresenter;

    @Bind(R.id.article_recycler_view) RecyclerView articleRecyclerView;
    @Bind(R.id.article_fast_scroller) FastScroller articleFastScroller;
    @Bind(R.id.empty_article_view) View emptyArticleView;
    @Bind(R.id.empty_article_description) TextView emptyArticleTextView;
    @Bind(R.id.empty_article_image) ImageView emptyArticleImageView;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void articleFetchCompleted(ArticleFetchCompletedEvent articleFetchCompletedEvent) {
        String url = articleFetchCompletedEvent.getUrl();
        updateView();

    }

    public BaseArticleFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_articles, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);

        articlePresenter.attachView(this);
        articlePresenter.loadAllArticles();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ARTICLE_OPTION) {
            String action = data.getAction();
            ArticleModel articleModel = (ArticleModel) data.getSerializableExtra(ArticleOptionDialog.SELECTED_ARTICLE);
            switch (action) {
                case "edit title":
                    BaseArticleDialog baseArticleDialog = BaseArticleDialog.newInstance(articleModel);
                    baseArticleDialog.setTargetFragment(BaseArticleFragment.this, REQUEST_EDIT_TITLE_OPTION);
                    baseArticleDialog.show(getFragmentManager(), TAG);

                    break;
                case "info":
                    ArticleInfoDialog articleInfoDialog = ArticleInfoDialog.newInstance(articleModel);
                    articleInfoDialog.show(getFragmentManager(), TAG);
                    break;
                case "delete":
                    DeleteArticleDialog deleteArticleDialog = DeleteArticleDialog.newInstance(articleModel);
                    deleteArticleDialog.setTargetFragment(BaseArticleFragment.this, DELETE_ARTICLE_OPTION);
                    deleteArticleDialog.show(getFragmentManager(), TAG);
                    break;
            }
        }
        if (requestCode == REQUEST_EDIT_TITLE_OPTION) {
            ArticleModel articleModel = (ArticleModel) data.getSerializableExtra(BaseArticleDialog.ARTICLE_MODEL_DATA);
            articlePresenter.updateArticle(articleModel);
        }

        if (requestCode == DELETE_ARTICLE_OPTION) {
            ArticleModel articleModel = (ArticleModel) data.getSerializableExtra(BaseArticleDialog.ARTICLE_MODEL_DATA);
            articlePresenter.deleteArticle(articleModel);
        }
    }


    private void updateView() {
        articlePresenter.loadAllArticles();
    }


    @Override
    public void displayArticle(List<ArticleModel> articleModels) {
        updateUi(1);
        initRecyclerView(articleModels);
    }


    private void initRecyclerView(List<ArticleModel> articleModels) {
        List<ArticleModel> sortedArticles = Stream.of(articleModels)
                .sortBy(ArticleModel::getTitle)
                .toList();


        ArticleAdapter articleAdapter = new ArticleAdapter(getContext(), this);

        articleRecyclerView.setAdapter(articleAdapter);
        articleAdapter.setArticleData(sortedArticles);
        articleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        articleFastScroller.setRecyclerView(articleRecyclerView);

    }

    @Override
    public void displayEmptyArticlesView(String message) {
        updateUi(0);
    }

    /*
    * hides or show based on status
    *  0-> display emptyview
    *  1-> display articleview
    * */
    private void updateUi(int status) {
        switch (status) {
            case 0:
                emptyArticleView.setVisibility(View.VISIBLE);
                articleRecyclerView.setVisibility(View.GONE);
                articleFastScroller.setVisibility(View.GONE);
                break;
            case 1:
                emptyArticleView.setVisibility(View.GONE);
                articleRecyclerView.setVisibility(View.VISIBLE);
                articleFastScroller.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void launchTagEditorView(ArticleModel articleModel) {

    }

    @Override
    public void articleFavortieToggler(ArticleModel articleModel) {

    }

    @Override
    public void launchArticleView(ArticleModel articleModel) {
        Intent launchActivityView = new Intent(this.getContext(), DisplayArticleActivity.class);
        launchActivityView.setAction(articleModel.getPath());
        getContext().startActivity(launchActivityView);
    }

    @Override
    public void launchArticleOptionsView(ArticleModel articleModel) {
        ArticleOptionDialog articleOptDialog = ArticleOptionDialog.newInstance(articleModel);
        articleOptDialog.setTargetFragment(BaseArticleFragment.this, REQUEST_ARTICLE_OPTION);
        articleOptDialog.show(getFragmentManager(), TAG);
    }

    @Override
    public void displayUpdateView(String msg) {
        Snackbar snackbar = Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onArticleSelected(ArticleModel articleModel) {
        articlePresenter.onArticleSelected(articleModel);
    }

    @Override
    public void onArticleOptionsSelected(ArticleModel articleModel) {
        articlePresenter.onArticleOptionSelected(articleModel);
    }

    @Override
    public void onArticleFavoriteToggleSelected(ArticleModel articleModel) {
        articlePresenter.onFavoriteToggleSelected(articleModel);
    }

    @Override
    public void onStop() {
        super.onStop();
        articlePresenter.dettachView();
        EventBus.getDefault().unregister(this);
    }

}