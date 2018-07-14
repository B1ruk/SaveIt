package io.start.biruk.saveit.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.start.biruk.saveit.view.baseArticleView.ArticleFragment;
import io.start.biruk.saveit.view.tagsView.TagFragment;

/**
 * Created by biruk on 5/10/2018.
 */
public class MainAdapter extends FragmentPagerAdapter {

    String[] pageFragmentTitles = {"Articles", "Tags", "Favorites"};
    Fragment[] pageFragments = {ArticleFragment.newInstance(1), new TagFragment(), ArticleFragment.newInstance(2)};

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
