package io.start.biruk.saveit.view.searchView;

import java.util.List;

import io.start.biruk.saveit.model.db.ArticleModel;

/**
 * Created by biruk on 5/10/2018.
 */
public interface SearchView {
    void displayEmptyResultsView();

    void showSearchResults(List<ArticleModel> articleModels);

    void displayDefaultView();

    void onSearchQuery(String query);
}
