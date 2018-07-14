package io.start.biruk.saveit.view.tagsView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.ArticleListEvent;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.presenter.SelectedTagPresenter;
import io.start.biruk.saveit.view.baseArticleView.ArticleFragment;

public class SelectedTagActivity extends AppCompatActivity implements SelectedTagView{
    @Inject SelectedTagPresenter selectedTagPresenter;

    @Bind(R.id.tag_articles) FrameLayout selectedTagArticleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected);

        ButterKnife.bind(this);
        App.getAppComponent().inject(this);

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        selectedTagPresenter.attachView(this);

        String action = getIntent().getAction();
        if (action!=null){
            selectedTag(action);
        }
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void selectedTag(String tag) {
        selectedTagPresenter.loadArticles(tag);
    }

    @Override
    public void displayArticles(List<ArticleModel> articleModels) {
        getSupportFragmentManager().beginTransaction()
                .add(selectedTagArticleView.getId(), ArticleFragment.newInstance(0))
                .commit();

        EventBus.getDefault().postSticky(new ArticleListEvent(articleModels));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        selectedTagPresenter.detachView();
    }
}
