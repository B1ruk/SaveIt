package io.start.biruk.saveit.view.searchView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import io.start.biruk.saveit.R;
import io.start.biruk.saveit.model.db.ArticleModel;

public class SearchActivity extends AppCompatActivity implements SearchView{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    @Override
    public void displayEmptyResultsView() {

    }

    @Override
    public void showSearchResults(List<ArticleModel> articleModels) {

    }

    @Override
    public void displayDefaultView() {

    }

    @Override
    public void onSearchQuery(String query) {

    }
}
