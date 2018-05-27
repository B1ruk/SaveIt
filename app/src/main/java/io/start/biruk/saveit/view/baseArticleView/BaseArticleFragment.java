package io.start.biruk.saveit.view.baseArticleView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.ArticleFetchCompletedEvent;
import io.start.biruk.saveit.model.dao.ArticleDbRepository;
import io.start.biruk.saveit.model.dao.ArticleRepository;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.presenter.BaseArticlePresenter;
import io.start.biruk.saveit.view.articleView.articleAdapter.ArticleAdapter;
import io.start.biruk.saveit.view.articleView.articleOptions.ArticleInfoDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.ArticleOptionDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.BaseArticleDialog;
import io.start.biruk.saveit.view.articleView.articleOptions.DeleteArticleDialog;
import io.start.biruk.saveit.view.displayArticleView.DisplayArticleActivity;
import io.start.biruk.saveit.view.listener.ArticleClickListener;

/**
 * Created by biruk on 5/13/2018.
 */
public class BaseArticleFragment extends Fragment implements BaseArticleView {

    private static final String TAG = "BaseArticleFragment";
    private static final int REQUEST_ARTICLE_OPTION=0;

    @Bind(R.id.article_recycler_view) RecyclerView articleRecyclerView;
    @Bind(R.id.empty_article_view) View emptyArticleView;
    @Bind(R.id.empty_article_description) TextView emptyArticleTextView;
    @Bind(R.id.empty_article_image) ImageView emptyArticleImageView;

    private ArticleRepository articleRepository;
    private BaseArticlePresenter baseArticlePresenter;

    private ArticleClickListener articleClickListener = new ArticleClickListener() {
        @Override
        public void onArticleSelected(ArticleModel articleModel) {
            baseArticlePresenter.onArticleSelected(articleModel);
        }

        @Override
        public void onArticleOptionsSelected(ArticleModel articleModel) {
            baseArticlePresenter.onArticleOptionsSelected(articleModel);
        }

        @Override
        public void onArticleFavoriteToggleSelected(ArticleModel articleModel) {
            baseArticlePresenter.onArticleFavoriteToggleSelected(articleModel);
        }

        @Override
        public void onArticleTagSelected(ArticleModel articleModel) {
            baseArticlePresenter.onArticleTagSelected(articleModel);
        }
    };


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

        this.articleRepository = new ArticleDbRepository(getContext());
        this.baseArticlePresenter = new BaseArticlePresenter(this);

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
        updateView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode== Activity.RESULT_OK && requestCode==REQUEST_ARTICLE_OPTION){
            String action = data.getAction();
            ArticleModel articleModel = (ArticleModel) data.getSerializableExtra(ArticleOptionDialog.SELECTED_ARTICLE);
            switch (action){
                case "edit title":
                    BaseArticleDialog baseArticleDialog=BaseArticleDialog.newInstance(articleModel);
                    baseArticleDialog.show(getFragmentManager(),TAG);
                    break;
                case "info":
                    ArticleInfoDialog articleInfoDialog=ArticleInfoDialog.newInstance(articleModel);
                    articleInfoDialog.show(getFragmentManager(),TAG);
                    break;
                case "delete":
                    DeleteArticleDialog deleteArticleDialog=DeleteArticleDialog.newInstance(articleModel);
                    deleteArticleDialog.show(getFragmentManager(),TAG);
                    break;
            }
        }
    }

    private void updateView() {
        articleRepository.getAllArticles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<ArticleModel>>() {
                    @Override
                    public void onSuccess(@NonNull List<ArticleModel> articleModels) {
                        if (!articleModels.isEmpty()) {
                            displayArticle(articleModels);
                        } else {
                            displayEmptyArticlesView("empty articles");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "something went wrong", e);
                    }
                });

    }



    @Override
    public void displayArticle(List<ArticleModel> articleModels) {
        updateUi(1);
        initRecyclerView(articleModels);
    }


    private void initRecyclerView(List<ArticleModel> articleModels) {
        ArticleAdapter articleAdapter = new ArticleAdapter(getContext(),articleClickListener);

        articleRecyclerView.setAdapter(articleAdapter);
        articleAdapter.setArticleData(articleModels);
        articleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        ArticleOptionDialog articleOptDialog= ArticleOptionDialog.newInstance(articleModel);
        articleOptDialog.setTargetFragment(BaseArticleFragment.this,REQUEST_ARTICLE_OPTION);
        articleOptDialog.show(getFragmentManager(),TAG);
    }



    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }



}