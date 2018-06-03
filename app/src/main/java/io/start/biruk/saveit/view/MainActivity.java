package io.start.biruk.saveit.view;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.start.biruk.saveit.R;
import io.start.biruk.saveit.events.ArticleFetchCompletedEvent;
import io.start.biruk.saveit.service.ArticleFetcherService;
import io.start.biruk.saveit.util.FileUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Bind(R.id.main_toolbar) Toolbar mainToolbar;
    @Bind(R.id.main_tab_layout) TabLayout mainTabLayout;
    @Bind(R.id.main_view_pager) ViewPager mainViewPager;
    @Bind(R.id.drawer_layout) DrawerLayout drawerLayout;
    @Bind(R.id.main_navigation) NavigationView navigationView;

    private MainAdapter mainAdapter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void articleFetchCompleted(ArticleFetchCompletedEvent articleFetchCompletedEvent){
        String url = articleFetchCompletedEvent.getMsg();
        displayInfo(String.format("saved %s",url));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_view);


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

        setUpDrawerLayout();
        setUpTabLayout();
        navigationView.setNavigationItemSelectedListener(this::navItemClickListener);
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
        Snackbar snackbar = Snackbar.make(drawerLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        MenuItem searchBar = menu.findItem(R.id.main_search_bar);

        searchBar.setOnMenuItemClickListener(item -> {
            Log.d(TAG,"search laumched");
            return false;
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void setUpDrawerLayout() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                drawerLayout,
                mainToolbar,
                R.string.open_drawer,
                R.string.close_drawer);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void setUpTabLayout() {
        mainAdapter = new MainAdapter(getSupportFragmentManager());
        mainAdapter = new MainAdapter(getSupportFragmentManager());

        mainViewPager.setAdapter(mainAdapter);
        mainTabLayout.setupWithViewPager(mainViewPager);
    }

    public boolean navItemClickListener(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_articles:
                mainViewPager.setCurrentItem(0);
                break;
            case R.id.nav_tags:
                mainViewPager.setCurrentItem(1);
                break;
            case R.id.nav_favorites:
                mainViewPager.setCurrentItem(2);
                break;
            case R.id.nav_menu_settings:
                Log.d(TAG, "nav settings");
                break;
        }
        drawerLayout.closeDrawers();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
