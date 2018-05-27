package io.start.biruk.saveit.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.start.biruk.saveit.view.articleView.ArticlesFragment;
import io.start.biruk.saveit.view.baseArticleView.BaseArticleFragment;
import io.start.biruk.saveit.view.favoriteView.FavoriteFragment;
import io.start.biruk.saveit.view.tagsView.TagFragment;

/**
 * Created by biruk on 5/10/2018.
 */
public class MainAdapter extends FragmentStatePagerAdapter {

    String[] pageFragmentTitles = {"Articles", "Tags", "Favorites"};
    Fragment[] pageFragments = {new BaseArticleFragment(), new TagFragment(), new FavoriteFragment()};

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return pageFragments[position];
    }

    @Override
    public int getCount() {
        return pageFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageFragmentTitles[position];
    }
}
