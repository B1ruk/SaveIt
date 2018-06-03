package io.start.biruk.saveit.view.searchView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.view.widget.fastscroller.FastScroller;

public class SearchActivity extends AppCompatActivity implements SearchArticleView {


    @Bind(R.id.search_toolbar) Toolbar mainSearchToolbar;
    @Bind(R.id.empty_search_view) View emptySearchView;
    @Bind(R.id.search_list_view) View listSearView;
    @Bind(R.id.empty_article_search_description) TextView emptySearchResultsView;
    @Bind(R.id.article_search_recycler_view) RecyclerView searchRecyclerView;
    @Bind(R.id.article_search_fast_scroller) FastScroller fastScroller;
    @Bind(R.id.empty_search_article_image) ImageView emptySearchImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        setSupportActionBar(mainSearchToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchBar = menu.findItem(R.id.article_library_search);
        SearchView searchView = (SearchView) searchBar.getActionView();

        searchView.setIconified(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public void displayEmptyResultsView() {

    }

    @Override
    public void showSearchResults(List<ArticleModel> articleModels) {

    }

    @Override
    public void displayDefaultView() {
        listSearView.setVisibility(View.GONE);
        emptySearchView.setVisibility(View.VISIBLE);


        emptySearchResultsView.setText("search articles");
    }

    @Override
    public void onSearchQuery(String query) {

    }
}
