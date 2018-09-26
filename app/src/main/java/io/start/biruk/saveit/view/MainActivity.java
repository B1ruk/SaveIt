package io.start.biruk.saveit.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.ArticleFetchCompletedEvent;
import io.start.biruk.saveit.service.ArticleFetcherService;
import io.start.biruk.saveit.util.FileUtil;
import io.start.biruk.saveit.view.baseArticleView.ArticleFragment;
import io.start.biruk.saveit.view.dialog.AddUrlDialog;
import io.start.biruk.saveit.view.searchView.SearchActivity;
import io.start.biruk.saveit.view.settingsView.SettingsActivity;
import io.start.biruk.saveit.view.tagsView.TagFragment;

public class MainActivity extends BaseThemeActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.main_toolbar) Toolbar mainToolbar;
    @Bind(R.id.fragment_container) View fragmentContainer;
    @Bind(R.id.bottom_nav) BottomNavigationView bottomNav;
    @Bind(R.id.launch_add_url) FloatingActionButton launchAddUrlFab;

    private MainAdapter mainAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void articleFetchCompleted(ArticleFetchCompletedEvent articleFetchCompletedEvent) {
        String url = articleFetchCompletedEvent.getMsg();
        displayInfo(String.format("saved %s", url));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        initArticleFetcherService();

        checkStorage();

        initViews();

    }

    private void checkStorage() {
        FileUtil.createMainDirectory();
    }


    private void initViews() {
        setSupportActionBar(mainToolbar);

        setUpBottomNav();
        launchAddUrlFab.setOnClickListener(v -> launchAddUrlDialog());
    }

    public void launchAddUrlDialog() {

        AddUrlDialog addUrlDialog = new AddUrlDialog();
        addUrlDialog.show(getSupportFragmentManager(), TAG);
    }

    public void initArticleFetcherService() {
        Intent startArticleService = new Intent(this, ArticleFetcherService.class);
        startService(startArticleService);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String action = getIntent().getAction();
        if (action != null) {
            if (!action.startsWith("android")) {
                displayInfo(action);
            }
        }
    }

    private void displayInfo(String msg) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchBar = menu.findItem(R.id.main_search_bar);
        MenuItem settings = menu.findItem(R.id.menu_settings);

        settings.setOnMenuItemClickListener(item -> {

            Intent stAct = new Intent(this, SettingsActivity.class);
            startActivity(stAct);
            return true;
        });

        searchBar.setOnMenuItemClickListener(item -> {
            launchSearchView();
            return true;
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void launchSearchView() {
        Intent launchSearchResults = new Intent(this, SearchActivity.class);
        startActivity(launchSearchResults);
    }


    public void setUpBottomNav() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_nav_article:
                    bindFragment(ArticleFragment.newInstance(1));
                    break;
                case R.id.bottom_nav_tag:
                    bindFragment(new TagFragment());
                    break;
                case R.id.bottom_nav_fav:
                    bindFragment(ArticleFragment.newInstance(2));
                    break;
            }
            return true;
        });
    }

    public void bindFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentContainer.getId(), fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
