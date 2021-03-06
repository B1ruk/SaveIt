package io.start.biruk.saveit.view.baseArticleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.ArticleFetchCompletedEvent;
import io.start.biruk.saveit.events.ArticleListEvent;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.presenter.ArticlePresenter;
import io.start.biruk.saveit.view.articleView.articleAdapter.ArticleAdapter;
import io.start.biruk.saveit.view.articleView.articleOptions.AddTagDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.ArticleInfoDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.ArticleOptionDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.EditTagDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.EditTitleArticleDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.DeleteArticleDialog;
import io.start.biruk.saveit.view.displayArticleView.DisplayArticleActivity;
import io.start.biruk.saveit.view.listener.ArticleClickListener;

/**
 * Created by biruk on 5/13/2018.
 */
public class ArticleFragment extends Fragment implements ArticleView, ArticleClickListener {

    private static final String TAG = "ArticleFragment";

    private static final int REQUEST_ARTICLE_OPTION = 0;
    private static final int REQUEST_EDIT_TITLE_OPTION = 7;
    private static final int DELETE_ARTICLE_OPTION = 8;
    private static final int REQUEST_ADD_TAG = 4;
    private static final int REQUEST_EDIT_TAG_OPTION = 12;

    private Parcelable recyclerState;

    @Inject protected ArticlePresenter articlePresenter;
    @Inject protected Picasso picasso;

    @Bind(R.id.article_recycler_view) protected RecyclerView articleRecyclerView;
    @Bind(R.id.empty_article_view) protected View emptyArticleView;
    @Bind(R.id.empty_article_description) protected TextView emptyArticleTextView;
    @Bind(R.id.empty_article_image) protected ImageView emptyArticleImageView;

    private static final String DEFAULT_VIEW = "io.start.biruk.saveit.view.baseArticleView";

    /*
    *
    * 0->Search Results,Specific Tag View
    * 1->Default View
    * 2->Favorite View
    * */
    private int defaultView = -1;
    private List<ArticleModel> articleModels;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void articleFetchCompleted(ArticleFetchCompletedEvent articleFetchCompletedEvent) {
        updateView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void articleFromEvent(ArticleListEvent articleListEvent) {
        this.articleModels = articleListEvent.getArticleModels();
        updateView();
    }

    public ArticleFragment() {
    }

    /**
     * @param defaultView (0->search,tag,1->Default,2->favorite )
     */
    public static ArticleFragment newInstance(int defaultView) {
        ArticleFragment articleFragment = new ArticleFragment();
        Bundle args = new Bundle();

        args.putInt(DEFAULT_VIEW, defaultView);
        articleFragment.setArguments(args);

        return articleFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App.getAppComponent().inject(this);

        this.defaultView = getArguments().getInt(DEFAULT_VIEW);
        this.articleModels = new ArrayList<>();
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

        articlePresenter.attachView(this);
        EventBus.getDefault().register(this);
        updateView();

        Log.d(TAG, "onResume()");
    }

    @Override
    public void updateView() {

        switch (defaultView) {
            case 0:
                articlePresenter.loadArticles(articleModels);
                break;
            case 1:
                articlePresenter.loadAllArticles();
                break;
            case 2:
                articlePresenter.loadFavoriteArticles();
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_ARTICLE_OPTION) {
            String action = data.getAction();
            ArticleModel articleModel = (ArticleModel) data.getSerializableExtra(ArticleOptionDialog.SELECTED_ARTICLE);
            switch (action) {
                case "edit title":
                    EditTitleArticleDialog editTitleArticleDialog = EditTitleArticleDialog.newInstance(articleModel);
                    editTitleArticleDialog.setTargetFragment(ArticleFragment.this, REQUEST_EDIT_TITLE_OPTION);
                    editTitleArticleDialog.show(getFragmentManager(), TAG);
                    break;

                case "add tag":
                    AddTagDialog addTagDialog = AddTagDialog.newInstance(articleModel);
                    addTagDialog.setTargetFragment(ArticleFragment.this, REQUEST_ADD_TAG);
                    addTagDialog.show(getFragmentManager(), TAG);
                    break;
                case "edit tag":
                    EditTagDialog editTagDialog = EditTagDialog.newInstance(articleModel);
                    editTagDialog.setTargetFragment(ArticleFragment.this, REQUEST_EDIT_TAG_OPTION);
                    editTagDialog.show(getFragmentManager(), TAG);
                    break;
                case "info":
                    ArticleInfoDialog articleInfoDialog = ArticleInfoDialog.newInstance(articleModel);
                    articleInfoDialog.show(getFragmentManager(), TAG);
                    break;
                case "delete":
                    DeleteArticleDialog deleteArticleDialog = DeleteArticleDialog.newInstance(articleModel);
                    deleteArticleDialog.setTargetFragment(ArticleFragment.this, DELETE_ARTICLE_OPTION);
                    deleteArticleDialog.show(getFragmentManager(), TAG);
                    break;
            }
        }
        if (requestCode == REQUEST_EDIT_TITLE_OPTION || requestCode == REQUEST_EDIT_TAG_OPTION) {
            ArticleModel articleModel = (ArticleModel) data.getSerializableExtra(EditTitleArticleDialog.ARTICLE_MODEL_DATA);
            articlePresenter.updateArticle(articleModel);
        }

        if (requestCode == DELETE_ARTICLE_OPTION) {
            ArticleModel articleModel = (ArticleModel) data.getSerializableExtra(EditTitleArticleDialog.ARTICLE_MODEL_DATA);
            articlePresenter.deleteArticle(articleModel);
        }


        if (requestCode == REQUEST_ADD_TAG) {
            ArticleModel articleModel = (ArticleModel) data.getSerializableExtra(AddTagDialog.ARTICLE_MODEL_DATA);
            String tag = data.getAction();

            articlePresenter.updateArticle(articleModel, tag);
        }
    }

    @Override
    public void displayArticle(List<ArticleModel> articleModels) {
        updateUi(1);
        initRecyclerView(articleModels);
    }


    private void initRecyclerView(List<ArticleModel> articleModels) {

        ArticleAdapter articleAdapter = new ArticleAdapter(getContext(), this);

        articleRecyclerView.setAdapter(articleAdapter);
        articleAdapter.setArticleData(articleModels);
        articleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    @Override
    public void displayEmptyArticlesView(String message) {
        updateUi(0);

        picasso.load(R.drawable.book_outline)
                .resize(200, 200)
                .into(emptyArticleImageView);
        emptyArticleTextView.setText("no articles found");

    }

    /*
    * hides or show based on status
    *  0-> display emptyview
    *  1-> display articleview
    * */
    protected void updateUi(int status) {
        switch (status) {
            case 0:
                emptyArticleView.setVisibility(View.VISIBLE);
                articleRecyclerView.setVisibility(View.GONE);
                break;
            case 1:
                emptyArticleView.setVisibility(View.GONE);
                articleRecyclerView.setVisibility(View.VISIBLE);
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
        articleOptDialog.setTargetFragment(ArticleFragment.this, REQUEST_ARTICLE_OPTION);
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
        articlePresenter.detachView();
        EventBus.getDefault().unregister(this);
    }

}