package io.start.biruk.saveit.view.searchView;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.presenter.SearchPresenter;
import io.start.biruk.saveit.view.articleView.articleAdapter.ArticleAdapter;
import io.start.biruk.saveit.view.displayArticleView.DisplayArticleActivity;
import io.start.biruk.saveit.view.listener.ArticleClickListener;
import io.start.biruk.saveit.view.widget.fastscroller.FastScroller;

public class SearchActivity extends AppCompatActivity implements SearchArticleView {

    @Inject SearchPresenter searchPresenter;
    @Inject Picasso picasso;

    @Bind(R.id.search_toolbar) Toolbar mainSearchToolbar;
    @Bind(R.id.empty_search_view) View emptySearchView;
    @Bind(R.id.search_list_view) View listSearView;
    @Bind(R.id.empty_article_search_description) TextView emptySearchResultsView;
    @Bind(R.id.article_search_recycler_view) RecyclerView searchRecyclerView;
    @Bind(R.id.article_search_fast_scroller) FastScroller fastScroller;
    @Bind(R.id.empty_search_article_image) ImageView emptySearchImageView;


    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);
        App.getAppComponent().inject(this);

        setupToolbar();
        displayDefaultView();
    }

    private void setupToolbar() {
        setSupportActionBar(mainSearchToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchPresenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchBar = menu.findItem(R.id.article_library_search);
        SearchView searchView = (SearchView) searchBar.getActionView();

        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearchQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onSearchQuery(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void showSearchResults(List<ArticleModel> articleModels) {
        emptySearchView.setVisibility(View.GONE);
        listSearView.setVisibility(View.VISIBLE);

        ArticleAdapter articleAdapter = new ArticleAdapter(this, new ArticleClickListener() {
            @Override
            public void onArticleSelected(ArticleModel articleModel) {
                launchDisplayArticleView(articleModel);
            }

            @Override
            public void onArticleOptionsSelected(ArticleModel articleModel) {

            }

            @Override
            public void onArticleFavoriteToggleSelected(ArticleModel articleModel) {

            }
        });

        searchRecyclerView.setAdapter(articleAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fastScroller.setRecyclerView(searchRecyclerView);
        articleAdapter.setArticleData(articleModels);

    }

    private void launchDisplayArticleView(ArticleModel articleModel) {
        Intent launchActivityView = new Intent(this, DisplayArticleActivity.class);
        launchActivityView.setAction(articleModel.getPath());
        startActivity(launchActivityView);
    }


    @Override
    public void displayEmptyResultsView() {
        listSearView.setVisibility(View.GONE);
        emptySearchView.setVisibility(View.VISIBLE);

        picasso.load(R.drawable.book_outline)
                .resize(200, 200)
                .into(emptySearchImageView);

        emptySearchResultsView.setText("no articles found");
    }

    @Override
    public void displayDefaultView() {
        listSearView.setVisibility(View.GONE);
        emptySearchView.setVisibility(View.VISIBLE);

        picasso.load(R.drawable.ic_search_24dp)
                .resize(32, 32)
                .into(emptySearchImageView);

        emptySearchResultsView.setText("search articles");
    }

    @Override
    public void onSearchQuery(String query) {
        searchPresenter.loadArticleSearchResults(query);
    }

    @Override
    protected void onStop() {
        super.onStop();

        searchPresenter.detachView(this);
    }
}
