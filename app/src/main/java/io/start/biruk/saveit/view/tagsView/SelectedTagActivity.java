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
import io.start.biruk.saveit.view.BaseThemeActivity;
import io.start.biruk.saveit.view.baseArticleView.ArticleFragment;

public class SelectedTagActivity extends BaseThemeActivity implements SelectedTagView {
    @Inject SelectedTagPresenter selectedTagPresenter;

    @Bind(R.id.tag_articles) FrameLayout selectedTagArticleView;
    @Bind(R.id.selected_tag_toolbar) Toolbar mainToolbar;

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
        attachFragment();
        String tag = getIntent().getAction();
        if (tag != null) {
            selectedTag(tag);
            setTagTitle(tag);
        }
    }

    private void attachFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(selectedTagArticleView.getId(), ArticleFragment.newInstance(0))
                .commit();
    }

    private void initViews() {
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setTagTitle(String tag) {
        mainToolbar.setTitle(tag);
    }

    @Override
    public void selectedTag(String tag) {
        selectedTagPresenter.loadArticles(tag);
    }

    @Override
    public void displayArticles(List<ArticleModel> articleModels) {
        EventBus.getDefault().post(new ArticleListEvent(articleModels));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        selectedTagPresenter.detachView();
    }
}
