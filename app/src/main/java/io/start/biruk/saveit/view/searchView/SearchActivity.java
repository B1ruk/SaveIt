package io.start.biruk.saveit.view.searchView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.App;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.ArticleListEvent;
import io.start.biruk.saveit.model.db.ArticleModel;
import io.start.biruk.saveit.presenter.SearchPresenter;
import io.start.biruk.saveit.view.BaseThemeActivity;
import io.start.biruk.saveit.view.baseArticleView.ArticleFragment;

public class SearchActivity extends BaseThemeActivity implements SearchArticleView {

    @Inject SearchPresenter searchPresenter;
    @Inject Picasso picasso;

    @Bind(R.id.search_toolbar) Toolbar mainSearchToolbar;

    @Bind(R.id.search_results_view) View searchResultsContainer;

    private static final String TAG = "SearchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);

        setupToolbar();
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
    public void onSearchQuery(String query) {
        searchPresenter.loadArticleSearchResults(query);
    }


    @Override
    public void showSearchResults(List<ArticleModel> articleModels) {

        getSupportFragmentManager().beginTransaction()
                .add(searchResultsContainer.getId(), ArticleFragment.newInstance(0))
                .commit();

        EventBus.getDefault().post(new ArticleListEvent(articleModels));

    }

    @Override
    protected void onStop() {
        super.onStop();

        searchPresenter.detachView(this);
    }
}
